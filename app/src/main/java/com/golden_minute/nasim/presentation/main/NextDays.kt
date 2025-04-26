package com.golden_minute.nasim.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.golden_minute.nasim.presentation.utils.getWeatherAppearance
import dev.chrisbanes.haze.HazeState
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
    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues( top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 24.dp, bottom = 100.dp),
        overscrollEffect = null, verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                activityViewModel.forecastDays.forEachIndexed { index, forecastDayItem ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(3.dp)
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                selectedIndex = index
                                activityViewModel.changeForecastDay(forecastDayItem.date.substring(8..9).toInt())
                            }
                    ) {
                        when (index) {
                            0 -> {
                                Text("Tomorrow")
                            }
                            else -> {
                                val day =
                                    activityViewModel.weatherState.value?.location?.localtime?.substring(
                                        8..9
                                    )?.toInt()?.plus(index + 1).toString()
                                Text(day)
                            }
                        }

                        Icon(
                            painter = painterResource(
                                getWeatherAppearance(
                                    forecastDayItem.day.condition.code,
                                    forecastDayItem.astro.isSunUp,
                                    true
                                )
                            ), contentDescription = "", modifier = Modifier.size(40.dp)
                        )
                        Text("${forecastDayItem.day.avgtempC.roundToInt()}°")
                    }
                }
            }
        }
        item {
            activityViewModel.weatherState.value?.location?.let {
                MainWeatherInfoSection(
                    isLoading = false,
                    weatherCode = getWeatherAppearance(
                        activityViewModel.forecastDays[selectedIndex].day.condition.code,
                        activityViewModel.forecastDays[selectedIndex].astro.isSunUp,
                        false
                    ),
                    activityViewModel = activityViewModel,
                    weatherStatus = activityViewModel.weatherState.value?.current?.condition?.text.toString(),
                    location = it.name,
                    temp = activityViewModel.weatherState.value!!.current?.tempC!!,
                    feelsLike = activityViewModel.weatherState.value!!.current?.feelslikeC!!,
                    hazeState = hazeState,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                    onSearchItemClicked = {},
                    navController = navController,
                    isDay = activityViewModel.weatherState.value!!.current!!.isDay
                )
            }
        }
        item {
            DetailSection(
                isLoading = false,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                hazeState = hazeState,
                minTemp = activityViewModel.weatherState.value?.forecast?.forecastday[selectedIndex]?.day?.mintempC.toString(),
                maxTemp = activityViewModel.weatherState.value?.forecast?.forecastday[selectedIndex]?.day?.maxtempC.toString(),
                windSpeed = activityViewModel.weatherState.value?.current?.windKph.toString(),
                windDegree = activityViewModel.weatherState.value?.current?.windDir.toString(),
                humidity = activityViewModel.weatherState.value?.current?.humidity.toString(),
                uv = activityViewModel.weatherState.value?.current?.cloud.toString()
            )
        }
        item {
            AstrosSection(hazeState = hazeState, activityViewModel = activityViewModel, modifier = Modifier.fillMaxWidth().padding(horizontal =  30.dp))
        }
        item {
            NextHoursForecastSection(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp), isLoading = false, hazeState = hazeState, navController = navController, nextHoursForecast = activityViewModel.nextHours)
        }
    }
}