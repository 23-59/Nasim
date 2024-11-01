package com.golden_minute.nasim.data

import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import com.golden_minute.nasim.domain.CoordinateResponse
import com.golden_minute.nasim.domain.CoordinateResponseItem
import com.golden_minute.nasim.domain.WeatherRequestService
import com.golden_minute.nasim.domain.WeatherResponse
import com.golden_minute.nasim.domain.utils.HttpRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class WeatherResponseServiceImpl(
    private val httpClient: HttpClient,
    private val coordinateDataStore: CoordinateDataStore
) : WeatherRequestService {

    override suspend fun getWeather(): List<WeatherResponse> {

        return try {
            httpClient.get {
                url(HttpRoutes.WEATHER_BASE_URL)
                parameter("lat", coordinateDataStore.getLatitude())
                parameter("lon", coordinateDataStore.getLongitude())
                parameter("appid", HttpRoutes.API_KEY)
            }.body()
        } catch (e:Exception){
            emptyList()
        }

    }

    override suspend fun getCoordinates(cityName: String): List<CoordinateResponseItem> {
        return httpClient.get {
            url(HttpRoutes.COORDINATE_BASE_URL)
            parameter("q", cityName)
            parameter("limit","8")
            parameter("appid", HttpRoutes.API_KEY)
        }.body()
    }
}