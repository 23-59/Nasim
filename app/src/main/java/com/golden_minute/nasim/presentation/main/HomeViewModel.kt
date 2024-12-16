package com.golden_minute.nasim.presentation.main

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.golden_minute.nasim.R
import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import com.golden_minute.nasim.domain.WeatherResponseType
import com.golden_minute.nasim.domain.model.weather_response.ForecastDayItem
import com.golden_minute.nasim.domain.model.weather_response.WeatherResponse
import com.golden_minute.nasim.domain.use_case.AppUseCases
import com.golden_minute.nasim.presentation.utils.getWeatherIcon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.math.roundToInt

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(application: Application, appUseCases: AppUseCases) :
    AndroidViewModel(application) {

    private val _state = mutableStateOf<WeatherResponse?>(null)
    val state: State<WeatherResponse?>
        get() = _state

    var selectedItem = mutableStateOf("Home")


    var nextHours = mutableStateListOf(Triple("", 0, ""))


    init {
        viewModelScope.launch {

            when (val result = appUseCases.getWeather(days = 1)) {

                is WeatherResponseType.Error -> Toast.makeText(
                    application.baseContext,
                    result.error,
                    Toast.LENGTH_SHORT
                ).show()

                is WeatherResponseType.OK -> {

                    _state.value = result.response
                    val currentTime =
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    val twoDaysNextHours = result.response.forecast?.forecastday?.take(2)
                    var firstDayValue: ForecastDayItem? = null
                    twoDaysNextHours?.forEachIndexed { index, forecastDayItem ->
                        if (index == 0) {
                            firstDayValue = forecastDayItem
                        }
                        if (index == 0 && currentTime.hour < 12) {
                            nextHours = forecastDayItem.hour.filter {
                                val apiTime = kotlinx.datetime.LocalDateTime(
                                    year = it.time.substring(0..3).toInt(),
                                    monthNumber = it.time.substring(5..6).toInt(),
                                    dayOfMonth = it.time.substring(8..9).toInt(),
                                    hour = it.time.substring(11..12).toInt(),
                                    minute = it.time.substring(14..15).toInt()
                                )

                                (apiTime > currentTime)

                            }.map {
                                Triple(
                                    "${it.tempC.roundToInt()}°",
                                    getWeatherIcon(weatherCode = it.condition.code,it.isDay,true),
                                    it.time.substring(11..15)
                                )
                            }.toMutableStateList()

                        }
                        else {
                            val secondDay = forecastDayItem.hour.filter { it.time.substring(11..12).toInt() <=currentTime.hour }
                            val firstDay = firstDayValue?.hour?.filter { it.time.substring(11..12).toInt() >= currentTime.hour }
                            val mixedHours = firstDay?.plus(secondDay)

                            nextHours = mixedHours?.map {
                                Triple(
                                    "${it.tempC.roundToInt()}°",
                                    getWeatherIcon(weatherCode =it.condition.code,it.isDay,true),
                                    it.time.substring(11..15)
                                )
                            }!!.toMutableStateList()
                        }
                        Log.i(TAG, ": ")
                    }
                }

            }

        }


    }
}