package com.golden_minute.nasim.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CoordinateResponse(

	@SerialName("CoordinateResponse")
	val coordinateResponse: List<CoordinateResponseItem?>? = null
)


@Serializable
data class CoordinateResponseItem(

	@SerialName("country")
	val country: String? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("lon")
	val lon: Float? = null,

	@SerialName("state")
	val state: String? = null,

	@SerialName("lat")
	val lat: Float? = null,

)
