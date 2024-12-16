package com.golden_minute.nasim.domain.model.weather_response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AirQuality(

	@SerialName("no2")
	val no2: Float,

	@SerialName("o3")
	val o3: Float,

	@SerialName("us-epa-index")
	val usEpaIndex: Int,

	@SerialName("so2")
	val so2: Float,

	@SerialName("pm2_5")
	val pm25: Float,

	@SerialName("pm10")
	val pm10: Float,

	@SerialName("co")
	val co: Float,

	@SerialName("gb-defra-index")
	val gbDefraIndex: Int
)