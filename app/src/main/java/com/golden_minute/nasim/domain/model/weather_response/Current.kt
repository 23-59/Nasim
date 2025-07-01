package com.golden_minute.nasim.domain.model.weather_response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Current(

    @SerialName("feelslike_c")
    val feelslikeC: Float?,

    @SerialName("feelslike_f")
    val feelslikeF: Float?,

    @SerialName("wind_degree")
    val windDegree: Int,

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

    @SerialName("humidity")
    val humidity: Int,

    @SerialName("uv")
    val uv: Float,

    @SerialName("last_updated")
    val lastUpdated: String,

    @SerialName("heatindex_f")
    val heatindexF: Float,

    @SerialName("is_day")
    val isDay: Int,

    @SerialName("precip_in")
    val precipIn: Float,

    @SerialName("heatindex_c")
    val heatindexC: Float,

    @SerialName("air_quality")
    val airQuality: AirQuality?,

    @SerialName("wind_dir")
    val windDir: String,

    @SerialName("pressure_in")
    val pressureIn: Float,

    @SerialName("precip_mm")
    val precipMm: Float,

    @SerialName("condition")
    val condition: Condition,

    @SerialName("pressure_mb")
    val pressureMb: Float,
)