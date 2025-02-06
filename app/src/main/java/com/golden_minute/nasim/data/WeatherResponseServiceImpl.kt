package com.golden_minute.nasim.data

import android.util.Log
import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import com.golden_minute.nasim.domain.CoordinateResponseType
import com.golden_minute.nasim.domain.WeatherRequestService
import com.golden_minute.nasim.domain.WeatherResponseType
import com.golden_minute.nasim.domain.utils.HttpRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kotlinx.coroutines.flow.first

private const val TAG = "WeatherResponseServiceI"

class WeatherResponseServiceImpl(
    private val httpClient: HttpClient,
    private val coordinateDataStore: CoordinateDataStore
) : WeatherRequestService {

    override suspend fun getWeather(days: Int, lat: Double?, lon: Double?): WeatherResponseType {

        try {

            return WeatherResponseType.OK(httpClient.get {
                url(HttpRoutes.WEATHER_BASE_URL)
                parameter("key", HttpRoutes.API_KEY)
                parameter(
                    "q",
                    if (lat == null && lon == null) "${coordinateDataStore.getLatitude.first()},${coordinateDataStore.getLongitude.first()}" else "$lat,$lon"
                )
                parameter("days", days)
                parameter("aqi", "yes")
            }
                .body())
        } catch (e: RedirectResponseException) {
            return WeatherResponseType.Error("Server error")
        } catch (e: HttpRequestTimeoutException) {
            return WeatherResponseType.Error("Internet connection timeout,Please check your internet connection and try again")
        } catch (e: ClientRequestException) {
            return WeatherResponseType.Error("Invalid request")
        } catch (e: ServerResponseException) {
            return WeatherResponseType.Error("Server error")
        } catch (e: Exception) {
            Log.i(TAG, e.message.toString())
            return WeatherResponseType.Error("An error has occurred,Please check your internet connection and try again.")
        }
    }

    override suspend fun getCitiesInfo(cityName: String): CoordinateResponseType {
        try {
            return CoordinateResponseType.OK(lat = null, lon = null, places = httpClient.get {
                url(HttpRoutes.COORDINATE_BASE_URL)
                parameter("q", cityName)
                parameter("key", HttpRoutes.API_KEY)
            }
                .body())

        } catch (e: RedirectResponseException) {
            return CoordinateResponseType.Error("Server Error")
        } catch (e: HttpRequestTimeoutException) {
            return CoordinateResponseType.Error("Internet Connection Timeout, Please Check Your Internet Connection")
        } catch (e: ClientRequestException) {
            return CoordinateResponseType.Error("Invalid Request")
        } catch (e: ServerResponseException) {
            return CoordinateResponseType.Error("Server Error")
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return CoordinateResponseType.Error("an error has occurred")
        }
    }

}