package com.golden_minute.nasim.domain.model.weather_response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Condition(

	@SerialName("code")
	val code: Int,

	@SerialName("icon")
	val icon: String,

	@SerialName("text")
	val text: String
)