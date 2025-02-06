package com.golden_minute.nasim.domain

interface WeatherRequestService {

    suspend fun getWeather(days:Int,lat:Double?,lon:Double?): WeatherResponseType

    suspend fun getCitiesInfo(cityName:String): CoordinateResponseType

}