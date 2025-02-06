package com.golden_minute.nasim.domain.model.coordinate_response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CoordinateResponse(

	@SerialName("CoordinateResponse")
	val coordinateResponse: List<SearchResponse>
)