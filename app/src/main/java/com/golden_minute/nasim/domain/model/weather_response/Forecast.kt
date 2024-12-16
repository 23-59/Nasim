package com.golden_minute.nasim.domain.model.weather_response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Forecast(

	@SerialName("forecastday")
	val forecastday: List<ForecastDayItem>
)