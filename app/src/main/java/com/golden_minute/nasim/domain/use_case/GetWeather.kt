package com.golden_minute.nasim.domain.use_case

import com.golden_minute.nasim.domain.WeatherRequestService
import com.golden_minute.nasim.domain.WeatherResponseType


class GetWeather(private val weatherRequestService: WeatherRequestService) {

    suspend operator fun invoke(days: Int): WeatherResponseType {
        return weatherRequestService.getWeather(days)
    }
}