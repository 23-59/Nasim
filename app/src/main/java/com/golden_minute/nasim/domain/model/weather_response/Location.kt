package com.golden_minute.nasim.domain.model.weather_response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Location(

	@SerialName("localtime")
	val localtime: String,

	@SerialName("country")
	val country: String,

	@SerialName("localtime_epoch")
	val localtimeEpoch: Int,

	@SerialName("name")
	val name: String,

	@SerialName("lon")
	val lon: Double,

	@SerialName("region")
	val region: String,

	@SerialName("lat")
	val lat: Double,

	@SerialName("tz_id")
	val tzId: String
)