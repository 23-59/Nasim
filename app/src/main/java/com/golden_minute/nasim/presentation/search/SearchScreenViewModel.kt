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
import com.golden_minute.nasim.presentation.main.IsDisconnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchScreenViewModel (private val appUseCases: AppUseCases) : ViewModel() {
    private var _searchValue = mutableStateOf("")
    val searchValue: State<String> = _searchValue

    var _weatherState = mutableStateOf<WeatherResponse?>(null)
    val weatherState: State<WeatherResponse?> = _weatherState


    private var _weatherResultListState = mutableStateListOf<WeatherResponse>()
    val weatherListState: SnapshotStateList<WeatherResponse> = _weatherResultListState

    var showLoadingState = mutableStateOf(false)
        private set

    var showClearButton = mutableStateOf(false)


    fun onEvent(events: SearchScreenEvents) {
            when (events) {
                is SearchScreenEvents.OnClickSearchedResult -> {
                    _weatherState.value = null
                    _weatherState.value = events.weatherItem
                }

                is SearchScreenEvents.OnSearchValueChanges -> {


                    _weatherResultListState.clear()
                    _searchValue.value = events.value



                    viewModelScope.launch(Dispatchers.IO) {
                        if (events.value.isNotBlank())
                            when (val coordinateResult =
                                appUseCases.getSearchedCitiesInfo(searchValue.value)) {
                                is CoordinateResponseType.Error -> {
                                    withContext(Dispatchers.Main) {
                                        IsDisconnected.isDisconnected.value = coordinateResult.error
                                    }
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
                                                withContext(Dispatchers.Main) {
                                                    IsDisconnected.isDisconnected.value = events.value
                                                }

                                                break
                                            }

                                            is WeatherResponseType.OK -> {
                                                if (_weatherResultListState.none { it.location!!.lat == weatherResult.response.location!!.lat })
                                                    _weatherResultListState.add(weatherResult.response)

                                            }
                                        }

                                        if (city.lat == coordinateResult.places.last().lat)
                                            withContext(Dispatchers.Main) {
                                                showLoadingState.value = false
                                            }

                                    }


                                }
                            }
                        else
                            withContext(Dispatchers.Main) {
                                showClearButton.value = false
                                _searchValue.value = ""
                                _weatherResultListState.clear()
                            }
                    }


                }

                SearchScreenEvents.OnClearTextField -> {
                    showClearButton.value = false
                    _searchValue.value = ""
                    _weatherResultListState.clear()
                }
            }


    }
}