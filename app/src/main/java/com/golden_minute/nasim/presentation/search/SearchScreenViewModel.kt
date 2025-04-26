package com.golden_minute.nasim.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.golden_minute.nasim.domain.CoordinateResponseType
import com.golden_minute.nasim.domain.WeatherResponseType
import com.golden_minute.nasim.domain.model.weather_response.WeatherResponse
import com.golden_minute.nasim.domain.use_case.AppUseCases
import com.golden_minute.nasim.presentation.main.ActivityViewModel
import com.golden_minute.nasim.presentation.main.IsDisconnected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val appUseCases: AppUseCases) :
    ViewModel() {
    private var _searchValue = mutableStateOf("")
    val searchValue: State<String> = _searchValue

    var _weatherState = mutableStateOf<WeatherResponse?>(null)
    val weatherState: State<WeatherResponse?> = _weatherState


    private var _weatherListState = mutableStateListOf<WeatherResponse>()
    val weatherListState: SnapshotStateList<WeatherResponse> = _weatherListState

    var showLoadingState = mutableStateOf(false)
        private set

    var showClearButton = mutableStateOf(false)


    fun onEvent(events: SearchScreenEvents) {
        viewModelScope.launch {
            when (events) {
                is SearchScreenEvents.OnClickSearchedResult -> {
                    _weatherState.value = null
                    _weatherState.value = events.weatherItem
                }

                is SearchScreenEvents.OnSearchValueChanges -> {


                    _weatherListState.clear()
                    _searchValue.value = events.value

                    if (_searchValue.value.isNotBlank())
                        when (val coordinateResult =
                            appUseCases.getSearchedCitiesInfo(searchValue.value)) {

                            is CoordinateResponseType.Error -> {
                                IsDisconnected.isDisconnected.value = coordinateResult.error
                            }

                            is CoordinateResponseType.OK -> {
                                if (IsDisconnected.isDisconnected.value.isNotBlank())
                                    IsDisconnected.isDisconnected.value = ""
                                if (coordinateResult.places.isNotEmpty())
                                    showClearButton.value = true
                                showLoadingState.value = true
                                for (city in coordinateResult.places) {

                                    when (val weatherResult =
                                        appUseCases.getWeather(1, city.lat, city.lon)) {
                                        is WeatherResponseType.Error -> {
                                            IsDisconnected.isDisconnected.value = events.value
                                            break
                                        }

                                        is WeatherResponseType.OK -> {
                                            if (_weatherListState.none { it.location!!.lat == weatherResult.response.location!!.lat })
                                                _weatherListState.add(weatherResult.response)

                                        }
                                    }
                                    if (city.lat == coordinateResult.places.last().lat)
                                        showLoadingState.value = false
                                }


                            }
                        }
                }

                SearchScreenEvents.OnClearTextField -> {
                    showClearButton.value = false
                    _searchValue.value = ""
                    _weatherListState.clear()
                }
            }
        }

    }
}