package com.golden_minute.nasim.presentation.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gigamole.composeshadowsplus.rsblur.rsBlurShadow
import com.gigamole.composeshadowsplus.softlayer.softLayerShadow
import com.golden_minute.nasim.R
import com.golden_minute.nasim.domain.model.weather_response.AirQuality
import com.golden_minute.nasim.presentation.utils.CustomPullToRefresh
import com.golden_minute.nasim.presentation.utils.DestinationRoutes
import com.golden_minute.nasim.presentation.utils.GlowingIcon
import com.golden_minute.nasim.presentation.utils.WeatherDetailElement
import com.golden_minute.nasim.presentation.utils.getWeatherAppearance
import com.golden_minute.nasim.presentation.utils.oneTimeShimmer
import com.golden_minute.nasim.presentation.utils.shimmerEffect
import com.golden_minute.nasim.ui.theme.PrimaryGreen
import com.golden_minute.nasim.ui.theme.fontFamily
import com.golden_minute.nasim.ui.theme.fontFamilyBold
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlin.math.roundToInt

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
    paddingValues: PaddingValues,
    activityViewModel: ActivityViewModel,
    navController: NavController
) {
    val scrollState = rememberScrollState()


    var isRefreshing by rememberSaveable { mutableStateOf(false) }

    var pullToRefresh by rememberSaveable { mutableStateOf(false) }

    val animatePullToRefresh = animateColorAsState(
        if (pullToRefresh) MaterialTheme.colorScheme.primary else Color(0xff808080)
    )
    val animatePullToRefreshGlow = animateColorAsState(if (pullToRefresh) MaterialTheme.colorScheme.primary else Color.Transparent)

    CustomPullToRefresh(
        isRefreshing = isRefreshing,
        pulledToDistance = { pullToRefresh = it },
        canBePulled = {
            scrollState.value == 0
        },
        onRefresh = {
            isRefreshing = true
            activityViewModel.getWeather(activityViewModel.lat, activityViewModel.lon)
            isRefreshing = false

        }) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .hazeSource(hazeState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            AnimatedVisibility(visible = !isRefreshing) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp, top = 24.dp)
                        .oneTimeShimmer(
                            textColor = Color(0xff808080), highlightColor = Color(
                                0xFFFFFFFF
                            ), startAnimation = ActivityViewModel.animatePullToRefresh
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlowingIcon(
                        imageVector = painterResource(R.drawable.round_keyboard_double_arrow_down_24),
                        contentDescription = null,
                        glowColor = animatePullToRefreshGlow.value,
                        glowRadius = 5.dp,
                        iconTint = animatePullToRefresh.value
                    )
                    Text(
                        "Pull to refresh",
                        fontWeight = FontWeight.Bold,
                        color = animatePullToRefresh.value,
                        style = MaterialTheme.typography.titleMedium.copy( shadow = Shadow(
                            color = animatePullToRefreshGlow.value,
                            offset = Offset(0f, 0f),
                            blurRadius = 15f
                        ) )
                    )
                }
            }



            AnimatedContent(
                targetState = activityViewModel.contentIsLoaded.value,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = ""
            ) { showContent ->
                if (showContent) {
                    if (activityViewModel.weatherState.value != null) {
                        MainWeatherInfoSection(
                            isLoading = false,
                            weatherCode = activityViewModel.weatherState.value!!.current?.condition!!.code,
                            weatherStatus = activityViewModel.weatherState.value!!.current?.condition?.text
                                ?: "",
                            isDay = activityViewModel.weatherState.value!!.current?.isDay ?: 0,
                            location = "${activityViewModel.weatherState.value!!.location?.name}, ${activityViewModel.weatherState.value!!.location?.country}",
                            temp = activityViewModel.weatherState.value!!.current?.tempC!!,
                            feelsLike = activityViewModel.weatherState.value!!.current?.feelslikeC!!,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp, end = 30.dp, bottom = 8.dp),
                            navController = navController,
                            activityViewModel = activityViewModel,
                            day = activityViewModel.weatherState.value!!.location?.localtime?.substring(
                                8..9
                            )?.toInt()!!,
                            month = activityViewModel.weatherState.value!!.location?.localtime?.substring(
                                5..6
                            )?.toInt()!!,
                            year = activityViewModel.weatherState.value!!.location?.localtime?.substring(
                                0..3
                            )?.toInt()!!,
                        )
                    } else {
                        IsDisconnected.isDisconnected.value =
                            "An error has occurred,Please check your internet connection and try again."
                    }
                } else
                    MainWeatherInfoSection(
                        isLoading = true,
                        weatherCode = 1000,
                        weatherStatus = "",
                        isDay = 0,
                        location = "",
                        temp = 0f,
                        feelsLike = 0f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        navController = navController,
                        activityViewModel = activityViewModel,
                        day = 0,
                        month = 0,
                        year = 0
                    )
            }

            AnimatedContent(
                targetState = activityViewModel.contentIsLoaded.value,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = ""
            ) { showContent ->
                if (showContent) {
                    DetailSection(
                        isLoading = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        minTemp = "${
                            activityViewModel.weatherState.value?.forecast?.forecastday?.get(
                                0
                            )?.day?.mintempC?.roundToInt()
                        }°",
                        maxTemp = "${
                            activityViewModel.weatherState.value?.forecast?.forecastday?.get(
                                0
                            )?.day?.maxtempC?.roundToInt()
                        }°",
                        windDegree = activityViewModel.weatherState.value?.current?.windDir.toString(),
                        humidity = "${activityViewModel.weatherState.value?.current?.humidity}%",
                        windSpeed = "${activityViewModel.weatherState.value?.current?.windKph}kph",
                        uv = "${activityViewModel.weatherState.value?.current?.uv}"
                    )
                } else {
                    DetailSection(
                        isLoading = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        minTemp = "",
                        maxTemp = "",
                        windDegree = 0.toString(),
                        humidity = "",
                        windSpeed = "",
                        uv = ""
                    )
                }
            }


            AnimatedContent(
                targetState = activityViewModel.contentIsLoaded.value,
                transitionSpec = { fadeIn() togetherWith fadeOut() })
            { contentIsLoaded ->

                if (contentIsLoaded) {
                    AstrosSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        sunrise = activityViewModel.weatherState.value?.forecast?.forecastday?.first()?.astro?.sunrise.toString(),
                        sunset = activityViewModel.weatherState.value?.forecast?.forecastday?.first()?.astro?.sunset.toString()
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(100.dp)
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xff313131))
                                .border(
                                    shape = RoundedCornerShape(12.dp),
                                    width = 2.dp,
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color.White.copy(0.5f),
                                            Color.White.copy(0.2f)
                                        )
                                    )
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Box(
                                Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .shimmerEffect()
                            )
                            Box(
                                Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(15.dp)
                                    .clip(
                                        RoundedCornerShape(2.dp)
                                    )
                                    .shimmerEffect()
                            )
                            Box(
                                Modifier
                                    .fillMaxWidth(0.4f)
                                    .height(10.dp)
                                    .clip(
                                        RoundedCornerShape(2.dp)
                                    )
                                    .shimmerEffect()
                            )
                        }
                        Spacer(Modifier.weight(0.2f))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(100.dp)
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xff313131))
                                .border(
                                    shape = RoundedCornerShape(12.dp),
                                    width = 2.dp,
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color.White.copy(0.5f),
                                            Color.White.copy(0.2f)
                                        )
                                    )
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Box(
                                Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .shimmerEffect()
                            )
                            Box(
                                Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(15.dp)
                                    .clip(
                                        RoundedCornerShape(2.dp)
                                    )
                                    .shimmerEffect()
                            )
                            Box(
                                Modifier
                                    .fillMaxWidth(0.4f)
                                    .height(10.dp)
                                    .clip(
                                        RoundedCornerShape(2.dp)
                                    )
                                    .shimmerEffect()
                            )
                        }
                    }
                }
            }

            AnimatedContent(targetState = activityViewModel.contentIsLoaded.value) { contentIsLoaded ->

                if (contentIsLoaded)
                    NextHoursForecastSection(
                        isLoading = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        nextHoursForecast = activityViewModel.nextHours,
                        navController = navController
                    )
                else
                    NextHoursForecastSection(
                        isLoading = true,
                        modifier = Modifier,
                        navController = navController,
                        nextHoursForecast = listOf(
                            Triple("12°", 0, "12:00"),
                            Triple("18°", 0, "03:00"),
                            Triple("10°", 0, "06:00"),
                            Triple("7°", 0, "09:00"),
                            Triple("7°", 0, "12:00"),
                            Triple("10°", 0, "03:00"),
                            Triple("9°", 0, "06:00")

                        )
                    )
            }
            val progressValue =
                when (activityViewModel.weatherState.value?.current?.airQuality?.usEpaIndex) {
                    1 -> 0.16f
                    2 -> 0.32f
                    3 -> 0.48f
                    4 -> 0.64f
                    5 -> 0.80f
                    6 -> 0.96f
                    else -> 0f
                }

            AnimatedContent(activityViewModel.contentIsLoaded.value) { isLoaded ->
                if (isLoaded)
                    activityViewModel.weatherState.value?.current?.airQuality?.let {
                        AirQualitySection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 30.dp,
                                    end = 30.dp,
                                ),
                            airQuality = it,
                            activityViewModel = activityViewModel,
                            progressValue = progressValue
                        )
                    }
                else
                    AirQualitySection(
                        modifier = Modifier,
                        airQuality = null,
                        activityViewModel = activityViewModel,
                        progressValue = progressValue
                    )

            }
            Spacer(Modifier.height(95.dp))
        }
    }


}

@Composable
fun BottomNavigationSection(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
    navController: NavController
) {

    val items = listOf(
        "Settings" to R.drawable.settings_02,
        "Locations" to R.drawable.globe_05,
        "Search" to R.drawable.search_lg,
        "Home" to R.drawable.home_05
    )
    val animatedSelectedTabIndex by animateFloatAsState(
        targetValue = items.indexOf(items.find { it.first == ActivityViewModel.currentScreen.value })
            .toFloat(),
        label = "animatedSelectedTabIndex",
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioLowBouncy
        )
    )
    Box(modifier) {


        NavigationBar(
            containerColor = Color.Transparent,
            windowInsets = WindowInsets(left = 10.dp, right = 10.dp, bottom = 0.dp, top = 0.dp),
            modifier = Modifier
                .background(Color.Transparent)
                .height(75.dp)
                .hazeEffect(
                    state = hazeState,
                    style = HazeStyle(
                        backgroundColor = MaterialTheme.colorScheme.background,
                        tint = HazeTint(Color(0xFF505050).copy(0.25f)),
                        noiseFactor = 0f
                    )
                )
                .clip(RoundedCornerShape(15.dp))
                .border(1.dp, Color.White.copy(0.2f), RoundedCornerShape(15.dp))


        ) {

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route



            for ((destination, iconId) in items) {


                if (items.any { it.first == currentRoute.toString() })
                    ActivityViewModel.currentScreen.value = currentRoute.toString()


                NavigationBarItem(
                    interactionSource = null,
                    colors = NavigationBarItemColors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        selectedIndicatorColor = Color.Transparent,
                        unselectedIconColor = Color.White.copy(0.5f),
                        unselectedTextColor = Color.White.copy(0.5f),
                        disabledIconColor = Color.Transparent,
                        disabledTextColor = Color.Transparent
                    ),
                    selected = ActivityViewModel.currentScreen.value == destination,
                    onClick = {

                        when (destination) {

                            "Home" -> {

                                if (ActivityViewModel.currentScreen.value != destination)
                                    navController.navigate(DestinationRoutes.HOME_SCREEN.route) {
                                        popUpTo(DestinationRoutes.HOME_SCREEN.route)
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                            }

                            "Search" -> {
                                if (ActivityViewModel.currentScreen.value != destination)
                                    navController.navigate("SEARCH_SCREEN") {
                                        launchSingleTop = true
                                        popUpTo(DestinationRoutes.SEARCH_SCREEN.route) {
                                            saveState = true
                                        }
                                    }


                            }

                            "Settings" -> {}
                            "Locations" -> {}
                        }
                        ActivityViewModel.currentScreen.value = destination
                    },
                    icon = {
                        Icon(
                            painter = painterResource(iconId),
                            contentDescription = destination
                        )
                    },
                    label = {
                        Text(
                            destination,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    alwaysShowLabel = false
                )
            }

        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .blur(60.dp)

        ) {
            val tabWidth = size.width / items.size
            drawCircle(
                color = PrimaryGreen,
                radius = size.height / 2,
                center = Offset(
                    x = (tabWidth * animatedSelectedTabIndex) + tabWidth / 2,
                    y = size.height / 2
                )
            )
        }


    }


}

@Composable
fun DetailSection(
    isLoading: Boolean = false,
    modifier: Modifier,
    minTemp: String,
    maxTemp: String,
    windDegree: String,
    humidity: String,
    windSpeed: String,
    uv: String
) {
    if (isLoading) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.clip(RoundedCornerShape(12.dp))
            .background(Color(0xff313131))
            .border(
                shape = RoundedCornerShape(12.dp),
                width = 2.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        Color.White.copy(0.5f),
                        Color.White.copy(0.2f)
                    )
                )
            )
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {

                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                }

            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .shadow(elevation = 5.dp, shape = RoundedCornerShape(15.dp))
                .clip(RoundedCornerShape(15.dp))
                .then(
                    if (isSystemInDarkTheme())
                        Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color(
                                            0xFF2a2a2a
                                        ), Color(0xff1e1e1e)
                                    )
                                )
                            )
                            .border(0.5.dp, Color(0xff313131), RoundedCornerShape(15.dp))
                    else
                        Modifier
                            .background(MaterialTheme.colorScheme.surface)

                )


        ) {
            val firstRow = listOf(
                Triple(minTemp, R.drawable.min_temp, "Min Temp"),
                Triple(maxTemp, R.drawable.max_temp, "Max Temp"),
                Triple(windDegree, R.drawable.compass, "Wind Direction")
            )
            val secondRow = listOf(
                Triple(humidity, R.drawable.raindrops, "Humidity"),
                Triple(windSpeed, R.drawable.windsock, "Wind Speed"),
                Triple(uv, R.drawable.sun, "UV Index")
            )

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                firstRow.forEach { item ->
                    WeatherDetailElement(
                        weatherIcon = item.second,
                        weatherStatus = item.first,
                        weatherLabel = item.third,
                        iconAtTop = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                secondRow.forEach { item ->
                    WeatherDetailElement(
                        weatherIcon = item.second,
                        weatherStatus = item.first,
                        weatherLabel = item.third,
                        iconAtTop = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }


}

@Composable
fun NextHoursForecastSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    showNextDaysButton: Boolean = true,
    navController: NavController,
    nextHoursForecast: List<Triple<String, Int, String>>,
) {
    if (isLoading)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xff313131))
                .border(
                    shape = RoundedCornerShape(12.dp),
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.White.copy(0.5f),
                            Color.White.copy(0.2f)
                        )
                    )
                )
        ) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 12.dp, top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(30.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .shimmerEffect()
                )

                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .shimmerEffect()
                )
            }
            Spacer(Modifier.height(16.dp))
            LazyRow(
                modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items(nextHoursForecast) { item ->
                    Box(
                        Modifier
                            .height(100.dp)
                            .width(90.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .shimmerEffect()
                    )
                }
            }
        }
    else
        Column(
            modifier = modifier
                .shadow(elevation = 5.dp, shape = RoundedCornerShape(15.dp))
                .clip(RoundedCornerShape(12.dp))
                .then(
                    if (isSystemInDarkTheme())
                        Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf<Color>(
                                        Color(
                                            0xFF2a2a2a
                                        ), Color(0xff1e1e1e)
                                    )
                                )
                            )
                            .border(0.5.dp, Color(0xff313131), RoundedCornerShape(15.dp))
                    else
                        Modifier
                            .background(MaterialTheme.colorScheme.surface)

                )

        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Today",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White.copy(0.75f)
                )
                if (showNextDaysButton)
                    TextButton(
                        onClick = {
                            if (ActivityViewModel.currentScreen.value == "Home")
                            navController.navigate(DestinationRoutes.NEXT_DAYS_SCREEN_HOME.route)
                            else if (ActivityViewModel.currentScreen.value == "Search")
                                navController.navigate(DestinationRoutes.NEXT_DAYS_SCREEN_SEARCH.route)
                        },
                        interactionSource = null,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            style = MaterialTheme.typography.labelLarge.copy( shadow = Shadow(
                                color = MaterialTheme.colorScheme.primary,
                                offset = Offset(0f, 0f),
                                blurRadius = 12f
                            )),
                            color = MaterialTheme.colorScheme.primary,
                            text = "Next 2 Days",
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamily,
                            modifier = Modifier,

                        )
                        GlowingIcon(
                            imageVector = painterResource(R.drawable.round_keyboard_arrow_right_24),
                            contentDescription = null,
                            glowColor = MaterialTheme.colorScheme.primary,
                            glowRadius = 5.dp,
                            iconTint = MaterialTheme.colorScheme.primary
                        )
                    }

            }
            Spacer(Modifier.height(16.dp))
            LazyRow(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                items(nextHoursForecast) { item ->
                    Row(Modifier.fillMaxSize()) {
                        WeatherDetailElement(
                            item.second,
                            item.first,
                            item.third,
                            false,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        VerticalDivider(
                            Modifier.height(90.dp),
                            thickness = 1.dp,
                            color = Color.White.copy(0.2f)
                        )
                    }

                }
            }

        }
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun MainWeatherInfoSection(
    isLoading: Boolean = true,
    weatherCode: Int,
    activityViewModel: ActivityViewModel,
    isDay: Int,
    day: Int,
    month: Int,
    year: Int,
    weatherStatus: String,
    location: String,
    temp: Float,
    feelsLike: Float?,
    modifier: Modifier,
    onSearchItemClicked: () -> Unit = {},
    navController: NavController
) {
    if (isLoading) {
        ConstraintLayout(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xff313131))
                .border(
                    shape = RoundedCornerShape(12.dp),
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.White.copy(0.5f),
                            Color.White.copy(0.2f)
                        )
                    )
                )
        ) {
            val (weatherIconPosition, tempTextPosition, locationIconPosition, weatherStatusPosition, feelsLikeTextPosition, dateTextPosition, locationTextPosition) = createRefs()

            Box(
                modifier = Modifier
                    .constrainAs(tempTextPosition) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end, 16.dp)
                        bottom.linkTo(parent.bottom, 12.dp)
                        width = Dimension.value(60.dp)
                        height = Dimension.value(60.dp)
                    }
                    .clip(RoundedCornerShape(3.dp))
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .constrainAs(weatherIconPosition) {
                        top.linkTo(dateTextPosition.top)
                        start.linkTo(parent.start, 16.dp)
                        width = Dimension.value(70.dp)
                        height = Dimension.value(70.dp)
                    }
                    .clip(RoundedCornerShape(3.dp))
                    .shimmerEffect())

            Box(
                modifier = Modifier
                    .constrainAs(weatherStatusPosition) {
                        top.linkTo(weatherIconPosition.bottom, 8.dp)
                        start.linkTo(parent.start, 16.dp)
                        end.linkTo(feelsLikeTextPosition.start, 32.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.value(20.dp)
                    }
                    .clip(RoundedCornerShape(3.dp))
                    .shimmerEffect())
            Box(
                modifier = Modifier
                    .constrainAs(locationIconPosition) {
                        top.linkTo(weatherStatusPosition.bottom, 8.dp)
                        start.linkTo(weatherStatusPosition.start)
                        width = Dimension.value(15.dp)
                        height = Dimension.value(16.dp)
                    }
                    .clip(RoundedCornerShape(3.dp))
                    .shimmerEffect())
            Box(
                modifier = Modifier
                    .constrainAs(feelsLikeTextPosition) {
                        top.linkTo(tempTextPosition.bottom, 8.dp)
                        start.linkTo(tempTextPosition.start)
                        end.linkTo(tempTextPosition.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.value(16.dp)
                    }
                    .clip(RoundedCornerShape(3.dp))
                    .shimmerEffect())

            Box(
                modifier = Modifier
                    .constrainAs(dateTextPosition) {
                        top.linkTo(parent.top, 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.value(130.dp)
                        height = Dimension.value(12.dp)
                    }
                    .clip(RoundedCornerShape(3.dp))
                    .shimmerEffect())

            Box(
                modifier = Modifier
                    .constrainAs(locationTextPosition) {
                        top.linkTo(locationIconPosition.top)
                        start.linkTo(locationIconPosition.end, 6.dp)
                        bottom.linkTo(parent.bottom, 16.dp)
                        width = Dimension.value(100.dp)
                        height = Dimension.value(16.dp)
                    }
                    .clip(RoundedCornerShape(3.dp))
                    .shimmerEffect())
        }
    } else {
        ConstraintLayout(
            modifier = modifier
                .softLayerShadow(shape = RoundedCornerShape(15.dp), radius = 12.dp, spread = 5.dp, offset = DpOffset(0.dp,12.dp), color = Color(
                    0xFF0C0C0C
                )
                )
                .clip(RoundedCornerShape(15.dp))
                .then(
                    if (isSystemInDarkTheme())
                        Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf<Color>(
                                        Color(
                                            0xFF424242
                                        ), Color(0xff2a2a2a)
                                    )
                                )
                            )
                            .border(0.5.dp, Color(0xff404040), RoundedCornerShape(15.dp))
                    else
                        Modifier
                            .background(MaterialTheme.colorScheme.surface)

                )


        ) {
            val (weatherIconPosition, changeLocationTextPosition, tempTextPosition, moreInfoButtonPosition, locationIconPosition, weatherStatusPosition, feelsLikeTextPosition, dateTextPosition, locationTextPosition) = createRefs()

            Text(
                text = "${temp.roundToInt()}°",
                style = MaterialTheme.typography.displayLarge.copy(
                    shadow = Shadow(
                        color = Color.White,
                        offset = Offset(0f, 0f),
                        blurRadius = 20f
                    )
                ),
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                letterSpacing = TextUnit(3f, TextUnitType.Sp),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(tempTextPosition) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(parent.bottom, 12.dp)
                })


            Image(
                contentScale = ContentScale.FillBounds,
                painter = painterResource(getWeatherAppearance(weatherCode, isDay, false)),
                contentDescription = "Weather Icon",
                modifier = Modifier.constrainAs(weatherIconPosition) {
                    top.linkTo(dateTextPosition.top)
                    start.linkTo(parent.start, 16.dp)
                    width = Dimension.value(80.dp)
                    height = Dimension.value(80.dp)
                })
            Text(
                text = weatherStatus,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                fontFamily = fontFamilyBold,
                modifier = Modifier.constrainAs(weatherStatusPosition) {
                    top.linkTo(weatherIconPosition.bottom, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(feelsLikeTextPosition.start, 12.dp)
                    width = Dimension.fillToConstraints

                })
            Icon(
                painter = painterResource(R.drawable.map_marker_outline),
                contentDescription = "Location Icon",
                modifier = Modifier
                    .constrainAs(locationIconPosition) {
                        if (location.length < 22)
                            top.linkTo(weatherStatusPosition.bottom, 8.dp)
                        else
                            top.linkTo(weatherStatusPosition.bottom, 20.dp)

                        start.linkTo(weatherStatusPosition.start)
                        baseline.linkTo(weatherStatusPosition.baseline)
                        if (navController.currentDestination?.route != DestinationRoutes.SEARCH_SCREEN.route && navController.currentDestination?.route != DestinationRoutes.SEARCH_SCREEN_DETAILS.route) {
                            bottom.linkTo(parent.bottom, 50.dp)
                        } else if (navController.currentDestination?.route == DestinationRoutes.SEARCH_SCREEN_DETAILS.route)
                            bottom.linkTo(parent.bottom, 32.dp)
                    }
                    .size(20.dp)
            )
            Text(
                text = "feels like ${feelsLike?.roundToInt()}°",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.constrainAs(feelsLikeTextPosition) {
                    baseline.linkTo(tempTextPosition.baseline, 26.dp)
                    start.linkTo(tempTextPosition.start)
                    end.linkTo(tempTextPosition.end)
                    width = Dimension.fillToConstraints
                })
            val formattedDate = LocalDate(year, month, day)
            val format = LocalDate.Format {
                dayOfWeek(
                    names = DayOfWeekNames(
                        listOf(
                            "Monday",
                            "Tuesday",
                            "Wednesday",
                            "Thursday",
                            "Friday",
                            "Saturday",
                            "Sunday"
                        )
                    )
                )
                char(' ')
                dayOfMonth()
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
            val today = formattedDate.format(format)
            Text(
                text = today,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = TextUnit(1f, TextUnitType.Sp),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(dateTextPosition) {
                    top.linkTo(parent.top, 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Text(
                text = location,
                letterSpacing = TextUnit(0.5f, TextUnitType.Sp),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.constrainAs(locationTextPosition) {
                    top.linkTo(locationIconPosition.bottom)
                    bottom.linkTo(locationIconPosition.top)
                    start.linkTo(locationIconPosition.end, 3.dp)
                    end.linkTo(feelsLikeTextPosition.start, 24.dp)
                    width = Dimension.fillToConstraints
                })

            if (navController.currentDestination?.route == DestinationRoutes.HOME_SCREEN.route) {

                var showChangeLocationBottomSheet = rememberSaveable { mutableStateOf(false) }

                if (showChangeLocationBottomSheet.value)
                    ChangeLocation(
                        modifier = Modifier,
                        activityViewModel = activityViewModel,
                        navController = navController
                    ) {
                        showChangeLocationBottomSheet.value = false
                    }

                TextButton(
                    interactionSource = null,
                    onClick = { showChangeLocationBottomSheet.value = true },
                    modifier = Modifier.constrainAs(changeLocationTextPosition) {

                        bottom.linkTo(parent.bottom, (-4).dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    }) {
                    Text(
                        "Change Location",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            shadow = Shadow(
                                color = MaterialTheme.colorScheme.primary,
                                offset = Offset(0f, 0f),
                                blurRadius = 12f
                            )
                        ),
                        fontWeight = FontWeight.Bold
                    )
                    GlowingIcon(
                        imageVector = painterResource(R.drawable.round_keyboard_arrow_down_24),
                        contentDescription = null,
                        glowColor = MaterialTheme.colorScheme.primary,
                        glowRadius = 5.dp,
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (navController.currentDestination?.route == DestinationRoutes.SEARCH_SCREEN.route)
                Button(
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.primary.copy(0.3f)
                    ),
                    modifier = Modifier.constrainAs(moreInfoButtonPosition) {
                        top.linkTo(locationTextPosition.bottom, 24.dp)
                        start.linkTo(parent.start, 24.dp)
                        end.linkTo(parent.end, 24.dp)
                        bottom.linkTo(parent.bottom, 12.dp)
                        width = Dimension.fillToConstraints
                    },
                    onClick = {
                        onSearchItemClicked()
                        navController.navigate(DestinationRoutes.SEARCH_SCREEN_DETAILS.route)
                    }) {
                    Text(
                        "More Details",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(2.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Icon(Icons.AutoMirrored.Default.ArrowForward, contentDescription = "")
                }

        }
    }
}

@Composable
fun AirQualitySection(
    modifier: Modifier,
    activityViewModel: ActivityViewModel,
    progressValue: Float,
    airQuality: AirQuality? = null,
) {
    var airPollutionTitle = ""
    var airPollutionDetail = ""
    var airPollutionProgressColor = Brush.verticalGradient(listOf(Color.Red, Color.Red))
    var airPollutionTextColor = Color.Red

    when (airQuality?.usEpaIndex) {
        1 -> {
            airPollutionTitle = "Good"
            airPollutionDetail =
                "Air quality is satisfactory, and air pollution poses little or no risk."
            airPollutionProgressColor =
                Brush.verticalGradient(listOf(Color(0xff00D80E), Color(0xff036200)))
            airPollutionTextColor = Color(0xff00D80E)
        }

        2 -> {
            airPollutionTitle = "Moderate"
            airPollutionDetail =
                "Air quality is acceptable. However, there may be a risk for some people, particularly those who are unusually sensitive to air pollution."
            airPollutionProgressColor =
                Brush.verticalGradient(listOf(Color(0xffD8AD00), Color(0xff624600)))
            airPollutionTextColor = Color(0xffD8AD00)
        }

        3 -> {
            airPollutionTitle = "Unhealthy for Sensitive Groups"
            airPollutionDetail =
                "Members of sensitive groups may experience health effects. The general public is less likely to be affected."
            airPollutionProgressColor =
                Brush.verticalGradient(listOf(Color(0xFFD85600), Color(0xFF623400)))
            airPollutionTextColor = Color(0xFFD85600)
        }

        4 -> {
            airPollutionTitle = "Unhealthy"
            airPollutionDetail =
                "Some members of the general public may experience health effects; members of sensitive groups may experience more serious health effects."
            airPollutionProgressColor =
                Brush.verticalGradient(listOf(Color(0xffD80000), Color(0xff621000)))
            airPollutionTextColor = Color(0xffD80000)
        }

        5 -> {
            airPollutionTitle = "Very Unhealthy"
            airPollutionDetail =
                "Health alert: The risk of health effects is increased for everyone."
            airPollutionProgressColor =
                Brush.verticalGradient(listOf(Color(0xff9F00D8), Color(0xff4F0062)))
            airPollutionTextColor = Color(0xff9F00D8)
        }

        6 -> {
            airPollutionTitle = "Hazardous"
            airPollutionDetail =
                "Health warning of emergency conditions: everyone is more likely to be affected."
            airPollutionProgressColor =
                Brush.verticalGradient(listOf(Color(0xFF95002F), Color(0xff4F001C)))
            airPollutionTextColor = Color(0xff95002A)
        }
    }
    AnimatedContent(activityViewModel.contentIsLoaded.value) {
        if (it) {
            Column(
                modifier = modifier
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(15.dp))
                    .clip(RoundedCornerShape(15.dp))
                    .then(
                        if (isSystemInDarkTheme())
                            Modifier
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf<Color>(
                                            Color(
                                                0xFF2a2a2a
                                            ), Color(0xff1e1e1e)
                                        )
                                    )
                                )
                                .border(0.5.dp, Color(0xff313131), RoundedCornerShape(15.dp))
                        else
                            Modifier
                                .background(MaterialTheme.colorScheme.surface)

                    )

            )
            {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.air_quality),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(18.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(.85f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .align(Alignment.CenterHorizontally)
                        .shadow(elevation = 5.dp, shape = RoundedCornerShape(15.dp))
                        .border(
                            width = 1.5.dp,
                            color = Color.White.copy(0.6f),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xff5C5C5C),
                                    Color(0xff424242)
                                )
                            ), shape = RoundedCornerShape(15.dp)
                        )
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(fraction = progressValue)
                            .height(50.dp)
                            .padding(vertical = 1.5.dp)
                            .align(Alignment.CenterStart)
                            .shadow(elevation = 16.dp, shape = RoundedCornerShape(15.dp))
                            .background(
                                airPollutionProgressColor,
                                shape = RoundedCornerShape(15.dp)
                            )

                    )
                    Text(
                        airPollutionTitle,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .align(
                                Alignment.Center
                            )
                    )

                }
                Spacer(Modifier.height(18.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = airPollutionDetail,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = airPollutionTextColor,
                )
                Spacer(Modifier.height(24.dp))
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "${airQuality?.co?.roundToInt()}/m³",
                            fontSize = 19.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "CO",
                            color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "${airQuality?.o3?.roundToInt()}/m³",
                            fontSize = 19.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "O3",
                            color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                            style = MaterialTheme.typography.bodyMedium

                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "${airQuality?.no2?.roundToInt()}/m³",
                            fontSize = 19.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "NO2",
                            color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(Modifier.height(32.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "${airQuality?.so2?.roundToInt()}/m³",
                            fontSize = 19.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "SO2",
                            color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "${airQuality?.pm25?.roundToInt()}/m³",
                            fontSize = 19.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "pm2.5",
                            color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${airQuality?.pm10?.roundToInt()}/m³",
                            fontSize = 19.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "pm10",
                            color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xff313131))
                    .border(
                        shape = RoundedCornerShape(12.dp),
                        width = 2.dp,
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.White.copy(0.5f),
                                Color.White.copy(0.2f)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(2.dp))
                Box(
                    Modifier
                        .fillMaxWidth(0.3f)
                        .height(30.dp)
                        .clip(
                            RoundedCornerShape(5.dp)
                        )
                        .shimmerEffect()
                )
                Box(
                    Modifier
                        .fillMaxWidth(0.7f)
                        .height(40.dp)
                        .clip(
                            RoundedCornerShape(5.dp)
                        )
                        .shimmerEffect()
                )
                Box(
                    Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                        .clip(
                            RoundedCornerShape(3.dp)
                        )
                        .shimmerEffect()
                )
                Box(
                    Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                        .clip(
                            RoundedCornerShape(3.dp)
                        )
                        .shimmerEffect()
                )
                Box(
                    Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                        .clip(
                            RoundedCornerShape(3.dp)
                        )
                        .shimmerEffect()
                )
                Box(
                    Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                        .clip(
                            RoundedCornerShape(3.dp)
                        )
                        .shimmerEffect()
                )
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            Modifier
                                .width(70.dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(8.dp))
                        Box(
                            Modifier
                                .width(90.dp)
                                .height(15.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerEffect()
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            Modifier
                                .width(70.dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(8.dp))
                        Box(
                            Modifier
                                .width(90.dp)
                                .height(15.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerEffect()
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            Modifier
                                .width(70.dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(8.dp))
                        Box(
                            Modifier
                                .width(90.dp)
                                .height(15.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerEffect()
                        )
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            Modifier
                                .width(70.dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(8.dp))
                        Box(
                            Modifier
                                .width(90.dp)
                                .height(15.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            Modifier
                                .width(70.dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(8.dp))
                        Box(
                            Modifier
                                .width(90.dp)
                                .height(15.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            Modifier
                                .width(70.dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(8.dp))
                        Box(
                            Modifier
                                .width(90.dp)
                                .height(15.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .shimmerEffect()
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }


}

@Composable
fun AstrosSection(
    modifier: Modifier = Modifier,
    sunrise: String,
    sunset: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .weight(1f)
                .shadow(
                    5.dp,
                    spotColor = Color.Black,
                    ambientColor = Color.Black,
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(15.dp))
                .then(
                    if (isSystemInDarkTheme())
                        Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf<Color>(
                                        Color(
                                            0xFF2a2a2a
                                        ), Color(0xff1e1e1e)
                                    )
                                )
                            )
                            .border(0.5.dp, Color(0xff313131), RoundedCornerShape(15.dp))
                    else
                        Modifier.background(MaterialTheme.colorScheme.surface)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onSurface.copy(0.7f),
                painter = painterResource(R.drawable.sunset),
                contentDescription = "sunrise",
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
            )
            Text(
                sunrise,
                letterSpacing = 3.sp,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                "sunrise",
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(0.4f)
            )
        }
        Spacer(Modifier.weight(0.2f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .weight(1f)
                .shadow(5.dp, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(15.dp))
                .then(
                    if (isSystemInDarkTheme())
                        Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf<Color>(
                                        Color(
                                            0xFF2a2a2a
                                        ), Color(0xff1e1e1e)
                                    )
                                )
                            )
                            .border(0.5.dp, Color(0xff313131), RoundedCornerShape(15.dp))
                    else
                        Modifier.background(MaterialTheme.colorScheme.surface)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onSurface.copy(0.7f),
                painter = painterResource(R.drawable.moonset),
                contentDescription = "sunrise",
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
            )

            Text(
                sunset,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                letterSpacing = 3.sp
            )

            Text(
                "sunset",
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(0.4f)
            )
        }

    }
}