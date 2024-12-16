package com.golden_minute.nasim.domain.model.weather_response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Astro(

	@SerialName("moonset")
	val moonset: String,

	@SerialName("moon_illumination")
	val moonIllumination: Int,

	@SerialName("sunrise")
	val sunrise: String,

	@SerialName("moon_phase")
	val moonPhase: String,

	@SerialName("sunset")
	val sunset: String,

	@SerialName("is_moon_up")
	val isMoonUp: Int,

	@SerialName("is_sun_up")
	val isSunUp: Int,

	@SerialName("moonrise")
	val moonrise: String
)