package com.golden_minute.nasim.domain.model.weather_response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class HourItem(

	@SerialName("feelslike_c")
	val feelslikeC: Float,

	@SerialName("feelslike_f")
	val feelslikeF: Float,

	@SerialName("wind_degree")
	val windDegree: Int,

	@SerialName("windchill_f")
	val windchillF: Float,

	@SerialName("windchill_c")
	val windchillC: Float,

	@SerialName("temp_c")
	val tempC: Float,

	@SerialName("temp_f")
	val tempF: Float,

	@SerialName("cloud")
	val cloud: Int,

	@SerialName("wind_kph")
	val windKph: Float,

	@SerialName("wind_mph")
	val windMph: Float,

	@SerialName("snow_cm")
	val snowCm: Float,

	@SerialName("humidity")
	val humidity: Int,


	@SerialName("will_it_rain")
	val willItRain: Int,

	@SerialName("uv")
	val uv: Float,

	@SerialName("is_day")
	val isDay: Int,

	@SerialName("wind_dir")
	val windDir: String,

	@SerialName("chance_of_rain")
	val chanceOfRain: Int,

	@SerialName("precip_mm")
	val precipMm: Float,

	@SerialName("condition")
	val condition: Condition,

	@SerialName("will_it_snow")
	val willItSnow: Int,

	@SerialName("time")
	val time: String,

	@SerialName("chance_of_snow")
	val chanceOfSnow: Int,

	@SerialName("pressure_mb")
	val pressureMb: Float,

	)