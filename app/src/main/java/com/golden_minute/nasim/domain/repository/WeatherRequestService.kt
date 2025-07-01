package com.golden_minute.nasim.domain.repository

import com.golden_minute.nasim.domain.CoordinateResponseType
import com.golden_minute.nasim.domain.WeatherResponseType

interface WeatherRequestService {

    suspend fun getWeather(days:Int,lat:Double?,lon:Double?): WeatherResponseType

    suspend fun getCitiesInfo(cityName:String): CoordinateResponseType

}