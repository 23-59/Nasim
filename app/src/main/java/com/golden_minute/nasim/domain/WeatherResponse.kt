package com.golden_minute.nasim.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class WeatherResponse(

	@SerialName("rain")
	val rain: Rain? = null,

	@SerialName("visibility")
	val visibility: Int? = null,

	@SerialName("timezone")
	val timezone: Int? = null,

	@SerialName("main")
	val main: Main? = null,

	@SerialName("clouds")
	val clouds: Clouds? = null,

	@SerialName("sys")
	val sys: Sys? = null,

	@SerialName("dt")
	val dt: Int? = null,

	@SerialName("coord")
	val coord: Coord? = null,

	@SerialName("weather")
	val weather: List<WeatherItem?>? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("cod")
	val cod: Int? = null,

	@SerialName("id")
	val id: Int? = null,

	@SerialName("base")
	val base: String? = null,

	@SerialName("wind")
	val wind: Wind? = null
)

@Serializable
data class Rain(

	@SerialName("1h")
	val jsonMember1h: Float? = null
)

@Serializable
data class WeatherItem(

	@SerialName("icon")
	val icon: String? = null,

	@SerialName("description")
	val description: String? = null,

	@SerialName("main")
	val main: String? = null,

	@SerialName("id")
	val id: Int? = null
)

@Serializable
data class Wind(

	@SerialName("deg")
	val deg: Int? = null,

	@SerialName("speed")
	val speed: Float? = null,

	@SerialName("gust")
	val gust: Float? = null
)

@Serializable
data class Coord(

	@SerialName("lon")
	val lon: Double? = null,

	@SerialName("lat")
	val lat: Double? = null
)

@Serializable
data class Main(

	@SerialName("temp")
	val temp: Float? = null,

	@SerialName("temp_min")
	val tempMin: Float? = null,
	@SerialName("temp_max")
	val tempMax: Float? = null,

	@SerialName("grnd_level")
	val grndLevel: Int? = null,

	@SerialName("humidity")
	val humidity: Int? = null,

	@SerialName("pressure")
	val pressure: Int? = null,

	@SerialName("sea_level")
	val seaLevel: Int? = null,

	@SerialName("feels_like")
	val feelsLike: Float? = null

)

@Serializable
data class Clouds(

	@SerialName("all")
	val all: Int? = null
)

@Serializable
data class Sys(

	@SerialName("country")
	val country: String? = null,

	@SerialName("sunrise")
	val sunrise: Int? = null,

	@SerialName("sunset")
	val sunset: Int? = null,

	@SerialName("id")
	val id: Int? = null,

	@SerialName("type")
	val type: Int? = null
)
