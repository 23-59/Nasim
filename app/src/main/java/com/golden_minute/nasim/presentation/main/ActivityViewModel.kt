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
import com.golden_minute.nasim.domain.model.weather_response.ForecastDayItem
import com.golden_minute.nasim.domain.model.weather_response.WeatherResponse
import com.golden_minute.nasim.domain.use_case.AppUseCases
import com.golden_minute.nasim.presentation.utils.getWeatherAppearance
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
import kotlin.math.roundToInt

private const val TAG = "ActivityViewModel"

object IsDisconnected {
    var isDisconnected = mutableStateOf("")
}

class ActivityViewModel (
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
        viewModelScope.launch(Dispatchers.Default) {

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

        viewModelScope.launch(Dispatchers.IO) {

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


    /**
     * Updates the weather information displayed based on the selected day.
     *
     * This function is called when a user selects a different day from the forecast.
     * It finds the weather data for the `targetDay` within the `forecastDays` list.
     * It then calculates the average "feels like" temperature in Celsius and Fahrenheit for that day.
     * It prepares a list of hourly weather information (temperature, weather icon, time) for the selected day.
     * Finally, it updates the `weatherState` with the information of the `selectedDay`,
     * including average temperatures, wind information, humidity, UV index, and weather condition.
     * The `nextHours` list is also updated to show the hourly forecast for the `selectedDay`.
     *
     * @param targetDay The day number (e.g., 25 for the 25th of the month) to display weather information for.
     */
    fun changeWeatherInfoDay(targetDay: Int) {
        val selectedDay = forecastDays.find { it.date.substring(8..9).toInt() == targetDay }
        val feelsLikeListC = selectedDay?.hour?.map { it.feelslikeC }
        val avgFeelsLikeC = feelsLikeListC?.average()?.toFloat()
        val feelsLikeListF = selectedDay?.hour?.map { it.feelslikeC }
        val avgFeelsLikeF = feelsLikeListF?.average()?.toFloat()
        val newHourItems = selectedDay!!.hour.map {
            Triple(
                "${it.tempC.roundToInt()}°",
                getWeatherAppearance(weatherCode = it.condition.code, it.isDay, true),
                it.time.substring(11..15)
            )
        }
        nextHours.clear()
        nextHours.addAll(newHourItems)

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
            )
        )
    }


    /**
     * Fetches weather data for the given coordinates and updates the UI.
     * This is an overloaded version of the `getWeather` function that takes latitude and longitude as parameters.
     *
     * This function performs the following actions:
     * 1. Sets `_contentIsLoaded` to `false` to indicate that data loading is in progress.
     * 2. Launches a coroutine in the IO dispatcher to perform the network request.
     * 3. Calls the `appUseCases.getWeather` function with the provided latitude, longitude, and number of days.
     * 4. Handles the `WeatherResponseType`:
     *     - If it's an `Error`, it sets `weatherState` to `null`, updates `IsDisconnected.isDisconnected` with the error message on the Main dispatcher, and logs the error.
     *     - If it's `OK`:
     *         - Updates `IsDisconnected.isDisconnected` to an empty string on the Main dispatcher.
     *         - Sets `weatherState` to the received weather response on the Main dispatcher.
     *         - Filters the forecast days to exclude the current day and updates `forecastDays` on the Main dispatcher.
     *         - Sets `_contentIsLoaded` to `true` on the Main dispatcher.
     *         - Processes the hourly forecast data:
     *             - If it's the first day and the current hour is before 12 PM, it populates `nextHours` with the forecast for the remaining hours of the current day.
     *             - Otherwise, it combines the remaining hours of the current day with the initial hours of the next day to populate `nextHours`.
     *         - If `isInNextDaysScreen` is true, it calls `changeWeatherInfoDay` to display the information for the first forecast day.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @param days The number of days for which to fetch the forecast (default is 3).
     * @param isInNextDaysScreen A boolean indicating whether the function is called from the "Next Days" screen.
     *                           If true, it will update the UI to display information for the first forecast day.
     */
    fun getWeather(lat: Double, lon: Double, days: Int = 3, isInNextDaysScreen: Boolean = false) {
        _contentIsLoaded.value = false
        viewModelScope.launch(Dispatchers.IO) {

            when (val result = appUseCases.getWeather(lat = lat, lon = lon, days = days)) {

                is WeatherResponseType.Error -> {
                    weatherState.value = null
                    withContext(Dispatchers.Main) {
                        IsDisconnected.isDisconnected.value = result.error
                    }
                    Log.i(TAG, "getWeather: ")

                }

                is WeatherResponseType.OK -> {

                    withContext(Dispatchers.Main) {
                        IsDisconnected.isDisconnected.value = ""
                        weatherState.value = result.response
                        forecastDays =
                            result.response.forecast?.forecastday?.filterIndexed { index, _ -> index != 0 }!!
                                .toMutableStateList()
                        _contentIsLoaded.value = true
                    }

                    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    lateinit var firstDayValue: ForecastDayItem
                    result.response.forecast?.forecastday?.forEachIndexed { index, forecastDayItem ->
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
                    if (isInNextDaysScreen)
                    changeWeatherInfoDay(forecastDays.first().date.substring(8..9).toInt())
                }

            }

        }
    }
}
