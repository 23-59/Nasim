package com.golden_minute.nasim.domain

interface WeatherRequestService {

    suspend fun getWeather(days:Int): WeatherResponseType

    suspend fun getCoordinates(cityName:String): CoordinateResponseType
}