package com.golden_minute.nasim.domain.use_case

import com.golden_minute.nasim.domain.CoordinateResponseType
import com.golden_minute.nasim.domain.WeatherRequestService

class GetSearchedCitiesInfo(private val weatherRequestService: WeatherRequestService) {

    suspend operator fun invoke(cityName: String): CoordinateResponseType {
        return weatherRequestService.getCitiesInfo(cityName)
    }
}