package com.golden_minute.nasim.domain.model.weather_response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WeatherResponse(

	@SerialName("current")
	val current: Current?,

	@SerialName("location")
	val location: Location?,

	@SerialName("forecast")
	val forecast: Forecast?
)