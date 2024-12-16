package com.golden_minute.nasim.domain.model.weather_response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Day(

	@SerialName("uv")
	val uv: Float,

	@SerialName("avgtemp_f")
	val avgtempF: Float,

	@SerialName("avgtemp_c")
	val avgtempC: Float,

	@SerialName("daily_chance_of_snow")
	val dailyChanceOfSnow: Int,

	@SerialName("maxtemp_c")
	val maxtempC: Float,

	@SerialName("maxtemp_f")
	val maxtempF: Float,

	@SerialName("mintemp_c")
	val mintempC: Float,

	@SerialName("daily_will_it_rain")
	val dailyWillItRain: Int,

	@SerialName("mintemp_f")
	val mintempF: Float,

	@SerialName("totalprecip_in")
	val totalprecipIn: Float,

	@SerialName("totalsnow_cm")
	val totalsnowCm: Float,

	@SerialName("avghumidity")
	val avghumidity: Int,

	@SerialName("condition")
	val condition: Condition,

	@SerialName("maxwind_kph")
	val maxwindKph: Float,

	@SerialName("maxwind_mph")
	val maxwindMph: Float,

	@SerialName("daily_chance_of_rain")
	val dailyChanceOfRain: Int,

	@SerialName("totalprecip_mm")
	val totalprecipMm: Float,

	@SerialName("daily_will_it_snow")
	val dailyWillItSnow: Int
)