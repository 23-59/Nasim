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
import com.golden_minute.nasim.domain.model.weather_response.Current
import com.golden_minute.nasim.domain.model.weather_response.Forecast
import com.golden_minute.nasim.domain.model.weather_response.ForecastDayItem
import com.golden_minute.nasim.domain.model.weather_response.WeatherResponse
import com.golden_minute.nasim.domain.use_case.AppUseCases
import com.golden_minute.nasim.presentation.utils.getWeatherAppearance
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.math.roundToInt

private const val TAG = "ActivityViewModel"

object IsDisconnected {
    var isDisconnected = mutableStateOf("")
}

@HiltViewModel
class ActivityViewModel @Inject constructor(
    application: Application,
    private val appUseCases: AppUseCases,
    private val coordinateDataStore: CoordinateDataStore
) :
    ViewModel() {
    var lat by mutableDoubleStateOf(0.0)
    var lon by mutableDoubleStateOf(0.0)
    var hazeState = HazeState()
    val hazeStateForSystemBars = HazeState()
    val hazeStateForBottomNavigation = HazeState()

    var forecastDays = mutableStateListOf<ForecastDayItem>()

    var imageRequest: ImageRequest? = null
    var turnOffSplashScreen = false
    var nextHours = mutableStateListOf(Triple("", 0, ""))
    var weatherState = mutableStateOf<WeatherResponse?>(null)


    private var _contentIsLoaded = mutableStateOf(false)
    val contentIsLoaded: State<Boolean>
        get() = _contentIsLoaded

    companion object {
        @JvmStatic
        var selectedItem = mutableStateOf("Home")
    }


    fun initializeWithDataStore() {
        viewModelScope.launch {

            //executes if the user is opening the app for the first time

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


    fun getWeather(days: Int = 3) {
        _contentIsLoaded.value = false

        viewModelScope.launch {

            when (val result = appUseCases.getWeather(lat = null, lon = null, days = days)) {

                is WeatherResponseType.Error -> {
                    weatherState.value = null
                    withContext(Dispatchers.Main) {
                        IsDisconnected.isDisconnected.value = result.error
                    }

                }

                is WeatherResponseType.OK -> {
                    weatherState.value = result.response

                        forecastDays =
                            result.response.forecast?.forecastday!!.filterIndexed { index, _ -> index != 0 }
                                .toMutableStateList()
                    withContext(Dispatchers.Main) {
                        IsDisconnected.isDisconnected.value = ""
                    }

                    _contentIsLoaded.value = true
                    val currentTime =
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    lateinit var firstDayValue: ForecastDayItem
                    result.response.forecast.forecastday.forEachIndexed { index, forecastDayItem ->
                        if (index == 0) {
                            firstDayValue = forecastDayItem
                        }
                        if (index == 0 && currentTime.hour < 12) {
                            nextHours = forecastDayItem.hour.filter {
                                val apiTime = LocalDateTime(
                                    year = it.time.substring(0..3).toInt(),
                                    monthNumber = it.time.substring(5..6).toInt(),
                                    dayOfMonth = it.time.substring(8..9).toInt(),
                                    hour = it.time.substring(11..12).toInt(),
                                    minute = it.time.substring(14..15).toInt()
                                )

                                (apiTime > currentTime)

                            }.map {
                                Triple(
                                    first = "${it.tempC.roundToInt()}°",
                                    second = getWeatherAppearance(
                                        weatherCode = it.condition.code,
                                        it.isDay,
                                        true
                                    ),
                                    third = it.time.substring(11..15)
                                )
                            }.toMutableStateList()
                            Log.i(TAG, ":$nextHours")
                        } else {
                            val secondDay = forecastDayItem.hour.filter {
                                it.time.substring(11..12).toInt() <= currentTime.hour
                            }
                            val firstDay = firstDayValue.hour.filter {
                                it.time.substring(11..12).toInt() >= currentTime.hour
                            }
                            val mixedHours = firstDay.plus(secondDay)

                            nextHours = mixedHours.map {
                                Triple(
                                    "${it.tempC.roundToInt()}°",
                                    getWeatherAppearance(
                                        weatherCode = it.condition.code,
                                        it.isDay,
                                        true
                                    ),
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

    fun changeForecastDay(targetDay: Int) {
        val selectedDay = forecastDays.find { it.date.substring(8..9).toInt() == targetDay }
        val feelsLikeListC = selectedDay?.hour?.map { it.feelslikeC }
        val avgFeelsLikeC = feelsLikeListC?.average()?.toFloat()
        val feelsLikeListF = selectedDay?.hour?.map { it.feelslikeC }
        val avgFeelsLikeF = feelsLikeListF?.average()?.toFloat()

        nextHours = selectedDay?.hour?.map {
            Triple(
                "${it.tempC.roundToInt()}°",
                getWeatherAppearance(weatherCode = it.condition.code, it.isDay, true),
                it.time.substring(11..15)
            )
        }?.toMutableStateList()!!

        val mostFrequentWindDir =
            selectedDay.hour.map { it.windDir }.groupingBy { it }.eachCount()
                .maxByOrNull { it.value }?.key

        weatherState.value = weatherState.value?.copy(
            current = Current(
                feelslikeC = avgFeelsLikeC,
                feelslikeF = avgFeelsLikeF,
                windDegree = 0,
                tempC = selectedDay.day.avgtempC,
                tempF = selectedDay.day.avgtempF,
                cloud = 0,
                windKph = selectedDay.day.maxwindKph,
                windMph = selectedDay.day.maxwindMph,
                humidity = selectedDay.day.avghumidity,
                uv = selectedDay.day.uv,
                lastUpdated = "",
                heatindexF = 0f,
                isDay = selectedDay.astro.isSunUp,
                precipIn = 0f,
                heatindexC = 0f,
                airQuality = null,
                windDir = mostFrequentWindDir.toString(),
                pressureIn = 0.0f,
                precipMm = 0.0f,
                condition = selectedDay.day.condition,
                pressureMb = 0.0f
            ), forecast = Forecast(forecastDays)
        )
    }

    fun getWeather(lat: Double, lon: Double, days: Int = 3) {
        _contentIsLoaded.value = false
        viewModelScope.launch {

            when (val result = appUseCases.getWeather(lat = lat, lon = lon, days = days)) {

                is WeatherResponseType.Error -> {
                    weatherState.value = null
                    withContext(Dispatchers.Main) {
                        IsDisconnected.isDisconnected.value = result.error
                    }
                    Log.i(TAG, "getWeather: ")

                }

                is WeatherResponseType.OK -> {
                    weatherState.value = result.response

                    Log.i(TAG, "getWeather: location is ${weatherState.value?.location?.name}")


                    forecastDays =
                        result.response.forecast?.forecastday?.filterIndexed { index, _ -> index != 0 }!!
                            .toMutableStateList()

                    withContext(Dispatchers.Main) {
                        IsDisconnected.isDisconnected.value = ""
                    }
                    Log.i(TAG, "getWeather: ")
                    _contentIsLoaded.value = true
                    val currentTime =
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    lateinit var firstDayValue: ForecastDayItem
                    result.response.forecast.forecastday.forEachIndexed { index, forecastDayItem ->
                        if (index == 0) {
                            firstDayValue = forecastDayItem
                        }
                        if (index == 0 && currentTime.hour < 12) {
                            nextHours = forecastDayItem.hour.filter {
                                val apiTime = LocalDateTime(
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
                                    getWeatherAppearance(
                                        weatherCode = it.condition.code,
                                        it.isDay,
                                        true
                                    ),
                                    it.time.substring(11..15)
                                )
                            }.toMutableStateList()
                            Log.i(TAG, ":$nextHours")
                        } else {
                            val secondDay = forecastDayItem.hour.filter {
                                it.time.substring(11..12).toInt() <= currentTime.hour
                            }
                            val firstDay = firstDayValue.hour.filter {
                                it.time.substring(11..12).toInt() >= currentTime.hour
                            }
                            val mixedHours = firstDay.plus(secondDay)

                            nextHours = mixedHours.map {
                                Triple(
                                    "${it.tempC.roundToInt()}°",
                                    getWeatherAppearance(
                                        weatherCode = it.condition.code,
                                        it.isDay,
                                        true
                                    ),
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
