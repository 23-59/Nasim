package com.golden_minute.nasim.domain.model.weather_response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ForecastDayItem(

	@SerialName("date")
	val date: String,

	@SerialName("astro")
	val astro: Astro,

	@SerialName("date_epoch")
	val dateEpoch: Int,

	@SerialName("hour")
	val hour: List<HourItem>,

	@SerialName("day")
	val day: Day
)