package com.golden_minute.nasim.domain

import com.golden_minute.nasim.domain.model.coordinate_response.CoordinateResponseItem
import com.golden_minute.nasim.domain.model.weather_response.WeatherResponse

sealed class WeatherResponseType {
    data class OK(val response:WeatherResponse):WeatherResponseType()
    data class Error(val error:String):WeatherResponseType()
}
sealed class CoordinateResponseType{
    data class OK(val places :List<CoordinateResponseItem>):CoordinateResponseType()
    data class Error(val error:String):CoordinateResponseType()
}