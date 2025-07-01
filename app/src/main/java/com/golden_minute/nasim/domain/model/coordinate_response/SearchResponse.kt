package com.golden_minute.nasim.domain.model.coordinate_response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SearchResponse(

	@SerialName("country")
	val country: String,

	@SerialName("name")
	val name: String,

	@SerialName("lon")
	val lon: Double,

	@SerialName("id")
	val id: Int,

	@SerialName("region")
	val region: String,

	@SerialName("lat")
	val lat: Double,

	@SerialName("url")
	val url: String
)