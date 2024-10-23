package com.golden_minute.nasim.domain.use_case

import com.golden_minute.nasim.domain.WeatherRequestService
import com.golden_minute.nasim.domain.WeatherResponse

class GetWeather(private val weatherRequestService: WeatherRequestService) {

    suspend operator fun invoke(): List<WeatherResponse> {
        return weatherRequestService.getWeather()
    }
}