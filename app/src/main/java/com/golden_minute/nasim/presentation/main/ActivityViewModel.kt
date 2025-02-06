package com.golden_minute.nasim.presentation.main

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.request.ImageRequest
import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import com.golden_minute.nasim.domain.WeatherResponseType
import com.golden_minute.nasim.domain.model.weather_response.ForecastDayItem
import com.golden_minute.nasim.domain.model.weather_response.WeatherResponse
import com.golden_minute.nasim.domain.use_case.AppUseCases
import com.golden_minute.nasim.presentation.utils.getWeatherAppearance
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.math.roundToInt

private const val TAG = "ActivityViewModel"

@HiltViewModel
class ActivityViewModel @Inject constructor(application: Application,private val appUseCases: AppUseCases,private val coordinateDataStore: CoordinateDataStore) :
    ViewModel() {
    var lat by mutableDoubleStateOf(0.0)
    var lon by mutableDoubleStateOf(0.0)
    var hazeState = HazeState()
    val hazeStateForSystemBars = HazeState()
    val hazeStateForNavigationBar = HazeState()
    var selectedItem = mutableStateOf("Home")
    var imageRequest: ImageRequest? = null
    var turnOffSplashScreen = false
    var nextHours = mutableStateListOf(Triple("", 0, ""))
    var weatherState = mutableStateOf<WeatherResponse?>(null)
    var isDisconnected = mutableStateOf("")

    private var _contentIsLoaded = mutableStateOf(false)
    val contentIsLoaded: State<Boolean>
        get() = _contentIsLoaded


    init {
        viewModelScope.launch {

            //checks if the user is opening the app for the time
            combine(
                coordinateDataStore.getLatitude,
                coordinateDataStore.getLongitude
            ) { latitude, longitude ->
                lat = latitude
                lon = longitude
                latitude != 0.0 && longitude != 0.0
            }.collectLatest { _ ->
                delay(1000)
                turnOffSplashScreen = true


            }

        }
    }
    fun getWeather() {
        _contentIsLoaded.value = false
        isDisconnected.value = ""
        viewModelScope.launch {

            when (val result = appUseCases.getWeather(lat = null, lon = null,days = 1)) {

                is WeatherResponseType.Error -> {
                    weatherState.value = null
                    isDisconnected.value = result.error
                }

                is WeatherResponseType.OK -> {
                    weatherState.value = result.response
                    _contentIsLoaded.value = true
                    val currentTime =
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    val twoDaysNextHours = result.response.forecast?.forecastday?.take(2)
                    lateinit var firstDayValue: ForecastDayItem
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
                                    getWeatherAppearance(weatherCode = it.condition.code,it.isDay,true),
                                    it.time.substring(11..15)
                                )
                            }.toMutableStateList()
                            Log.i(TAG, ":$nextHours")
                        }
                        else {
                            val secondDay = forecastDayItem.hour.filter { it.time.substring(11..12).toInt() <=currentTime.hour }
                            val firstDay = firstDayValue.hour.filter { it.time.substring(11..12).toInt() >= currentTime.hour }
                            val mixedHours = firstDay.plus(secondDay)

                            nextHours = mixedHours.map {
                                Triple(
                                    "${it.tempC.roundToInt()}°",
                                    getWeatherAppearance(weatherCode =it.condition.code,it.isDay,true),
                                    it.time.substring(11..15)
                                )
                            }.toMutableStateList()

                            Log.i(TAG, ":")
                        }
                    }

                }

            }

        }
    }
}
