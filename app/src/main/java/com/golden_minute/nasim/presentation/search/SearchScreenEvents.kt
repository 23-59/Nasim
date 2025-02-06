package com.golden_minute.nasim.presentation.search

import com.golden_minute.nasim.domain.model.weather_response.WeatherResponse

sealed class SearchScreenEvents {

    data class OnSearchValueChanges(val value:String):SearchScreenEvents()

    data class OnClickSearchedResult(val cityLatitude:Double):SearchScreenEvents()

    data object OnClearTextField:SearchScreenEvents()
}