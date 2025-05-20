package com.golden_minute.nasim.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.golden_minute.nasim.presentation.utils.getWeatherAppearance
import com.golden_minute.nasim.presentation.utils.glassEffect
import dev.chrisbanes.haze.HazeState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format

import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

import kotlin.math.roundToInt

@Composable
fun NextDaysScreen(
    modifier: Modifier = Modifier,
    activityViewModel: ActivityViewModel,
    navController: NavController
) {


    val lazyListState = rememberLazyListState()
    val hazeState = remember { HazeState() }
    var selectedIndex by remember { mutableIntStateOf(0) }

    val format = LocalDate.Format {
        char(' ')
        monthName(
            names = MonthNames(
                listOf(
                    "Jan",
                    "Feb",
                    "Mar",
                    "Apr",
                    "May",
                    "Jun",
                    "Jul",
                    "Aug",
                    "Sep",
                    "Oct",
                    "Nov",
                    "Dec"
                )
            )
        )
    }
    val localDate = LocalDate(
        year = activityViewModel.forecastDays[selectedIndex].date.substring(0..3).toInt(),
        monthNumber = activityViewModel.forecastDays[selectedIndex].date.substring(5..6).toInt(),
        dayOfMonth = activityViewModel.forecastDays[selectedIndex].date.substring(8..9).toInt()
    )
    val formattedDate = localDate.format(format)

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 24.dp,
            bottom = 100.dp
        ),
        overscrollEffect = null, verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                activityViewModel.forecastDays.forEachIndexed { index, forecastDayItem ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(5.dp)
                            .weight(1f).glassEffect(hazeState)


                            .clickable {

                                selectedIndex = index
                                activityViewModel.changeWeatherInfoDay(
                                    forecastDayItem.date.substring(8..9).toInt()
                                )
                            }
                            .padding(horizontal = 32.dp)
                    ) {

                                Text("${forecastDayItem.date.substring(8..9)}$formattedDate",modifier = Modifier.padding(top = 5.dp))

                        Icon(
                            painter = painterResource(
                                getWeatherAppearance(
                                    forecastDayItem.day.condition.code,
                                    forecastDayItem.astro.isSunUp,
                                    true
                                )
                            ), contentDescription = "", modifier = Modifier.size(40.dp)
                        )
                        Text("${forecastDayItem.day.avgtempC.roundToInt()}°",modifier = Modifier.padding(bottom = 5.dp))
                    }
                }
            }
        }
        item {
            activityViewModel.weatherState.value?.location?.let {
                MainWeatherInfoSection(
                    isLoading = false,
                    weatherCode = activityViewModel.forecastDays[selectedIndex].day.condition.code,
                    activityViewModel = activityViewModel,
                    weatherStatus = activityViewModel.weatherState.value?.current?.condition?.text.toString(),
                    location = it.name,
                    temp = activityViewModel.weatherState.value!!.current?.tempC!!,
                    feelsLike = activityViewModel.weatherState.value!!.current?.feelslikeC!!,
                    hazeState = hazeState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    onSearchItemClicked = {},
                    navController = navController,
                    isDay = activityViewModel.weatherState.value!!.current!!.isDay,
                    day = activityViewModel.forecastDays[selectedIndex].date.substring(8..9)
                        .toInt(),
                    month = activityViewModel.forecastDays[selectedIndex].date.substring(5..6)
                        .toInt(),
                    year = activityViewModel.forecastDays[selectedIndex].date.substring(0..3)
                        .toInt()
                )
            }
        }
        item {
            DetailSection(
                isLoading = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                hazeState = hazeState,
                minTemp = "${activityViewModel.weatherState.value?.forecast?.forecastday[selectedIndex]?.day?.mintempC?.roundToInt()}°",
                maxTemp = "${activityViewModel.weatherState.value?.forecast?.forecastday[selectedIndex]?.day?.maxtempC?.roundToInt()}°",
                windSpeed = "${activityViewModel.weatherState.value?.current?.windKph}kph",
                windDegree = activityViewModel.weatherState.value?.current?.windDir.toString(),
                humidity = "${activityViewModel.weatherState.value?.current?.humidity}%",
                uv = activityViewModel.weatherState.value?.current?.cloud.toString()
            )
        }
        item {
            AstrosSection(
                hazeState = hazeState,
                sunrise = activityViewModel.weatherState.value?.forecast?.forecastday[selectedIndex]?.astro?.sunrise.toString(),
                sunset = activityViewModel.weatherState.value?.forecast?.forecastday[selectedIndex]?.astro?.sunset.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )
        }
        item {
            NextHoursForecastSection(
                showNextDaysButton = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                isLoading = false,
                hazeState = hazeState,
                navController = navController,
                nextHoursForecast = activityViewModel.nextHours
            )
        }
    }
}