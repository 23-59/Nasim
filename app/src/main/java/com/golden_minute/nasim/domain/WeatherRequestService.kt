package com.golden_minute.nasim.domain

interface WeatherRequestService {

    suspend fun getWeather(): List<WeatherResponse>

    suspend fun getCoordinates(cityName:String,countryCode:String): CoordinateResponse
}