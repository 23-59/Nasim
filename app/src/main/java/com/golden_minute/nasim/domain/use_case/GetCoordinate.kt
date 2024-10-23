package com.golden_minute.nasim.domain.use_case

import com.golden_minute.nasim.domain.CoordinateResponse
import com.golden_minute.nasim.domain.WeatherRequestService

class GetCoordinate(private val weatherRequestService: WeatherRequestService) {

    suspend operator fun invoke(cityName: String, countryCode: String): CoordinateResponse {
        return weatherRequestService.getCoordinates(cityName, countryCode)
    }
}