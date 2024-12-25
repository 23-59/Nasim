package com.golden_minute.nasim.presentation.main

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import coil.size.Size
import com.golden_minute.nasim.R
import com.golden_minute.nasim.domain.model.weather_response.AirQuality
import com.golden_minute.nasim.presentation.utils.WeatherDetailElement
import com.golden_minute.nasim.presentation.utils.getWeatherAppearance
import com.golden_minute.nasim.presentation.utils.glassEffect
import com.golden_minute.nasim.presentation.utils.shimmerEffect
import com.golden_minute.nasim.ui.theme.PrimaryGreen
import com.golden_minute.nasim.ui.theme.fontFamily
import com.golden_minute.nasim.ui.theme.fontFamilyBold
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun HomePage(modifier: Modifier = Modifier, viewModel: HomeViewModel) {
    val scrollState = rememberScrollState()
    val hazeState = remember { HazeState() }
    val hazeStateForSystemBars = remember { HazeState() }
    val hazeStateForNavigationBar = remember { HazeState() }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .haze(hazeStateForSystemBars),
        bottomBar = {
                    BottomNavigationSection( viewModel = viewModel,
                        hazeState = hazeStateForNavigationBar,
                        modifier = modifier.padding(
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding(),
                            top = 16.dp,
                        )
                    )
        }) { _ ->

        Box(modifier = modifier.fillMaxSize().haze(hazeStateForNavigationBar)) {

                AsyncImage(
                   model = ImageRequest.Builder(LocalContext.current)
                       .data(viewModel.state.value?.current?.condition?.let {
                           viewModel.state.value?.current?.isDay?.let { it1 ->
                               getWeatherAppearance(
                                   it.code,
                                   it1
                               )
                           }
                       })
                       .precision(Precision.EXACT)
                       .build(),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                        .haze(hazeState)
                )

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(
                        top = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding(),
                        bottom = 160.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                AnimatedContent(
                    targetState = viewModel.state.value != null && viewModel.state.value!!.current != null && viewModel.state.value!!.location != null && viewModel.state.value!!.forecast != null,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = ""
                ) { showContent ->
                    if (showContent)
                        MainWeatherInfoSection(
                            isLoading = false,
                            weatherCode = viewModel.state.value!!.current?.condition!!.code,
                            weatherStatus = viewModel.state.value!!.current?.condition?.text ?: "",
                            isDay = viewModel.state.value!!.current?.isDay ?: 0,
                            location = viewModel.state.value!!.location?.name ?: "",
                            temp = viewModel.state.value!!.current?.tempC?.roundToInt()
                                .toString(),
                            feelsLike = viewModel.state.value!!.current?.feelslikeC?.roundToInt()
                                .toString(),
                            hazeState = hazeState,
                            modifier = modifier
                        )
                    else
                        MainWeatherInfoSection(
                            isLoading = true,
                            weatherCode = 1000,
                            weatherStatus = "",
                            isDay = 0,
                            location = "",
                            temp = 0.toString(),
                            feelsLike = 0.toString(),
                            hazeState = hazeState,
                            modifier = modifier
                        )
                }

                AnimatedContent(
                    targetState = viewModel.state.value != null && viewModel.state.value!!.current != null && viewModel.state.value!!.location != null && viewModel.state.value!!.forecast != null,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = ""
                ) { showContent ->
                    if (showContent) {
                        DetailSection(
                            isLoading = false,
                            modifier = modifier.padding(horizontal = 28.dp),
                            hazeState = hazeState,
                            minTemp = "${viewModel.state.value?.forecast?.forecastday?.get(0)?.day?.mintempC?.roundToInt()}°",
                            maxTemp = "${viewModel.state.value?.forecast?.forecastday?.get(0)?.day?.maxtempC?.roundToInt()}°",
                            windDegree = viewModel.state.value?.current?.windDegree.toString(),
                            humidity = "${viewModel.state.value?.current?.humidity}%",
                            windSpeed = "${viewModel.state.value?.current?.windKph}kph",
                            cloudCover = "${viewModel.state.value?.current?.cloud}%"
                        )
                    } else {
                        DetailSection(
                            isLoading = true,
                            modifier = modifier.padding(horizontal = 28.dp),
                            hazeState = hazeState,
                            minTemp = "",
                            maxTemp = "",
                            windDegree = 0.toString(),
                            humidity = "",
                            windSpeed = "",
                            cloudCover = ""
                        )
                    }
                }
                AnimatedContent(
                    targetState = viewModel.state.value != null && viewModel.state.value!!.current != null && viewModel.state.value!!.location != null && viewModel.state.value!!.forecast != null,
                    transitionSpec = { fadeIn() togetherWith fadeOut() })
                { contentIsLoaded ->

                    if (contentIsLoaded) {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .weight(1f)
                                    .glassEffect(hazeState),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.sunset),
                                    contentDescription = "sunrise",
                                    modifier = Modifier.padding(8.dp).size(40.dp)
                                )
                                viewModel.state.value?.forecast?.forecastday?.get(0)?.astro?.let {
                                    Text(
                                        it.sunrise,
                                        letterSpacing = 3.sp,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                                Text(
                                    "sunrise",
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    color = Color.White.copy(0.5f)
                                )
                            }
                            Spacer(Modifier.weight(0.2f))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .weight(1f)
                                    .glassEffect(hazeState),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.moonset),
                                    contentDescription = "sunrise",
                                    modifier = Modifier.padding(8.dp).size(40.dp)
                                )
                                viewModel.state.value?.forecast?.forecastday?.get(0)?.astro?.let {
                                    Text(
                                        it.sunset,
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleLarge,
                                        letterSpacing = 3.sp
                                    )
                                }
                                Text(
                                    "sunset",
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    color = Color.White.copy(0.5f)
                                )
                            }

                        }
                    } else {
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(100.dp)
                                    .weight(1f)
                                    .glassEffect(hazeState),
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
                                    .glassEffect(hazeState),
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

                AnimatedContent(targetState = viewModel.state.value != null && viewModel.state.value!!.current != null && viewModel.state.value!!.location != null && viewModel.state.value!!.forecast != null) { contentIsLoaded ->

                    if (contentIsLoaded)
                        NextHoursForecastSection(
                            isLoading = false,
                            modifier = modifier,
                            hazeState = hazeState,
                            nextHoursForecast = viewModel.nextHours
                        )
                    else
                        NextHoursForecastSection(
                            isLoading = true,
                            modifier = modifier,
                            hazeState = hazeState,
                            nextHoursForecast = listOf(
                                Triple("12°", 0, "12:00"),
                                Triple("18°", 0, "03:00"),
                                Triple("10°", 0, "06:00"),
                                Triple("7°",  0, "09:00"),
                                Triple("7°",  0, "12:00"),
                                Triple("10°", 0, "03:00"),
                                Triple("9°",  0, "06:00")

                            )
                        )
                }
                val progressValue = when(viewModel.state.value?.current?.airQuality?.usEpaIndex) {
                    1 -> 0.16f
                    2 -> 0.32f
                    3 -> 0.48f
                    4 -> 0.64f
                    5 -> 0.80f
                    6 ->0.96f
                    else -> 0f
                }

                viewModel.state.value?.current?.airQuality?.let { AirQualitySection(hazeState = hazeState, airQuality = it, progressValue = progressValue) }


            }
            val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            // glassmorphic status bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(statusBarHeight)
                    .align(Alignment.TopCenter)
                    .hazeChild(hazeStateForSystemBars, style = HazeStyle(noiseFactor = 0f))
            )
            // glassmorphic navigation bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.surface)


            )

        }

    }
}

@Composable
fun BottomNavigationSection(modifier: Modifier = Modifier,hazeState: HazeState,viewModel: HomeViewModel) {


    val items = listOf(
        "Settings" to R.drawable.settings_02,
        "Locations" to R.drawable.globe_05,
        "Search" to R.drawable.search_lg,
        "Home" to R.drawable.home_05
    )
    val animatedSelectedTabIndex by animateFloatAsState(
        targetValue = items.indexOf(items.find { it.first == viewModel.selectedItem.value }).toFloat(),
        label = "animatedSelectedTabIndex",
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioLowBouncy
        )
    )
    Box {
//
//       Box(Modifier.fillMaxWidth().align(Alignment.BottomCenter).height(140.dp).background(brush = Brush.verticalGradient(endY = 350f,
//           colors =  listOf(Color.Transparent,Color(0xff131313))
//       )))
        NavigationBar(
            tonalElevation = 5.dp,
            containerColor = Color.Transparent,
            windowInsets = WindowInsets(left = 10.dp, right = 10.dp, bottom = 0.dp, top = 0.dp),
            modifier = modifier
                .fillMaxWidth()
                .height(75.dp)
                .hazeChild(hazeState, style = HazeStyle(noiseFactor = 0f))



        ) {

            for ((key, value) in items) {

                NavigationBarItem(
                    colors = NavigationBarItemColors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        selectedIndicatorColor = Color.Transparent,
                        unselectedIconColor = Color.White.copy(0.5f),
                        unselectedTextColor = Color.White.copy(0.5f),
                        disabledIconColor = Color.Transparent,
                        disabledTextColor = Color.Transparent
                    ),
                    selected = viewModel.selectedItem.value == key,
                    onClick = { viewModel.selectedItem.value = key },
                    icon = { Icon(painter = painterResource(value), contentDescription = key) },
                    label = {
                        Text(
                            key,
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
                .padding( top = 16.dp)
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
    hazeState: HazeState,
    minTemp: String,
    maxTemp: String,
    windDegree: String,
    humidity: String,
    windSpeed: String,
    cloudCover: String
) {
    if (isLoading) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .fillMaxWidth()
                .glassEffect(hazeState)
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
                .fillMaxWidth()
                .glassEffect(hazeState = hazeState)

        ) {
            val firstRow = listOf(
                Triple(minTemp, R.drawable.min_temp, "Min Temp"),
                Triple(maxTemp, R.drawable.max_temp, "Max Temp"),
                Triple(windDegree, R.drawable.compass, "Wind Degree")
            )
            val secondRow = listOf(
                Triple(humidity, R.drawable.raindrops, "Humidity"),
                Triple(windSpeed, R.drawable.windsock, "Wind Speed"),
                Triple(cloudCover, R.drawable.clouds, "Cloud Cover")
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
                        modifier = Modifier
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
                        modifier = Modifier
                    )
                }
            }
        }
    }


}

@Composable
fun NextHoursForecastSection(
    isLoading: Boolean = true,
    modifier: Modifier = Modifier,
    hazeState: HazeState,
    nextHoursForecast: List<Triple<String, Int, String>>
) {
    if (isLoading)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .glassEffect(hazeState)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .glassEffect(hazeState)
        ) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 12.dp, top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Today",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        text = "Next 4 Days",
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        modifier = Modifier
                            .clickable { }
                            .padding(bottom = 2.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.chevron_right),
                        contentDescription = "Next 4 Days",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            LazyRow(
                modifier.fillMaxWidth(),
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
                            modifier = modifier
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                            VerticalDivider(
                                modifier.height(90.dp),
                                thickness = 1.dp,
                                color = Color.White.copy(0.2f)
                            )
                    }

                }
            }

        }
}


@Composable
fun MainWeatherInfoSection(
    isLoading: Boolean = true,
    weatherCode: Int,
    isDay:Int,
    weatherStatus: String,
    location: String,
    temp: String,
    feelsLike: String,
    hazeState: HazeState,
    modifier: Modifier
) {
    if (isLoading) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, top = 16.dp)
                .glassEffect(hazeState)
        ) {
            val (weatherIconPosition, tempTextPosition, locationIconPosition, weatherStatusPosition, feelsLikeTextPosition, dateTextPosition, locationTextPosition) = createRefs()

            Box(modifier = Modifier
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

            Box(modifier = Modifier
                .constrainAs(weatherIconPosition) {
                    top.linkTo(dateTextPosition.top)
                    start.linkTo(parent.start, 16.dp)
                    width = Dimension.value(70.dp)
                    height = Dimension.value(70.dp)
                }
                .clip(RoundedCornerShape(3.dp))
                .shimmerEffect())

            Box(modifier = Modifier
                .constrainAs(weatherStatusPosition) {
                    top.linkTo(weatherIconPosition.bottom, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(feelsLikeTextPosition.start, 32.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(20.dp)
                }
                .clip(RoundedCornerShape(3.dp))
                .shimmerEffect())
            Box(modifier = Modifier
                .constrainAs(locationIconPosition) {
                    top.linkTo(weatherStatusPosition.bottom, 8.dp)
                    start.linkTo(weatherStatusPosition.start)
                    width = Dimension.value(15.dp)
                    height = Dimension.value(16.dp)
                }
                .clip(RoundedCornerShape(3.dp))
                .shimmerEffect())
            Box(modifier = Modifier
                .constrainAs(feelsLikeTextPosition) {
                    top.linkTo(tempTextPosition.bottom, 8.dp)
                    start.linkTo(tempTextPosition.start)
                    end.linkTo(tempTextPosition.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(16.dp)
                }
                .clip(RoundedCornerShape(3.dp))
                .shimmerEffect())

            Box(modifier = Modifier
                .constrainAs(dateTextPosition) {
                    top.linkTo(parent.top, 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.value(130.dp)
                    height = Dimension.value(12.dp)
                }
                .clip(RoundedCornerShape(3.dp))
                .shimmerEffect())

            Box(modifier = Modifier
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
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, top = 16.dp)
                .glassEffect(hazeState)

        ) {
            val (weatherIconPosition, tempTextPosition, locationIconPosition, weatherStatusPosition, feelsLikeTextPosition, dateTextPosition, locationTextPosition) = createRefs()

            Text(
                text = "${temp.toFloat().roundToInt()}°",
                style = MaterialTheme.typography.displayLarge,
                letterSpacing = TextUnit(3f, TextUnitType.Sp),
                fontWeight = FontWeight.Bold,
                modifier = modifier.constrainAs(tempTextPosition) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(parent.bottom, 12.dp)
                })


            Image(contentScale = ContentScale.FillBounds,
                painter = painterResource(getWeatherAppearance(weatherCode,isDay,false)),
                contentDescription = "Weather Icon",
                modifier = modifier.constrainAs(weatherIconPosition) {
                    top.linkTo(dateTextPosition.top)
                    start.linkTo(parent.start, 16.dp)
                    width = Dimension.value(80.dp)
                    height = Dimension.value(80.dp)
                })
            Text(
                text = weatherStatus,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyBold,
                modifier = modifier.constrainAs(weatherStatusPosition) {
                    top.linkTo(weatherIconPosition.bottom, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(feelsLikeTextPosition.start, 12.dp)
                    width = Dimension.fillToConstraints

                })
            Icon(
                painter = painterResource(R.drawable.map_marker_outline),
                contentDescription = "Location Icon",
                modifier = modifier.constrainAs(locationIconPosition) {
                    top.linkTo(weatherStatusPosition.bottom, 4.dp)
                    start.linkTo(weatherStatusPosition.start)
                })
            Text(
                text = "feels like ${feelsLike.toFloat().roundToInt()}°",
                style = MaterialTheme.typography.bodyMedium,

                modifier = modifier.constrainAs(feelsLikeTextPosition) {
                    top.linkTo(tempTextPosition.bottom)

                    end.linkTo(parent.end, 16.dp)
                    width = Dimension.fillToConstraints
                })
            val formatter = DateTimeFormatter.ofPattern("'today', d MMM yyyy")
            val formattedDate = LocalDateTime.now().format(formatter)
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodyMedium,
                letterSpacing = TextUnit(1f, TextUnitType.Sp),
                fontWeight = FontWeight.Bold,
                modifier = modifier.constrainAs(dateTextPosition) {
                    top.linkTo(parent.top, 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Text(
                text = location,
                letterSpacing = TextUnit(0.5f, TextUnitType.Sp),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = modifier.constrainAs(locationTextPosition) {
                    top.linkTo(locationIconPosition.top, (-2).dp)
                    start.linkTo(locationIconPosition.end, 3.dp)
                    bottom.linkTo(parent.bottom, 12.dp)
                })

        }
    }
}

@Composable
fun AirQualitySection(modifier: Modifier = Modifier, hazeState: HazeState, progressValue: Float,airQuality: AirQuality) {
    var airPollutionTitle = ""
    var airPollutionDetail = ""
    var airPollutionProgressColor  = Brush.verticalGradient(listOf(Color.Red))
    var airPollutionTextColor  = Color.Red

    when(airQuality.usEpaIndex) {
        1 -> {
            airPollutionTitle = "Good"
            airPollutionDetail = "Air quality is satisfactory, and air pollution poses little or no risk."
            airPollutionProgressColor = Brush.verticalGradient(listOf(Color(0xff00D80E),Color(0xff036200)))
            airPollutionTextColor = Color(0xff00D80E)
        }
         2 -> {
             airPollutionTitle = "Moderate"
             airPollutionDetail = "Air quality is acceptable. However, there may be a risk for some people, particularly those who are unusually sensitive to air pollution."
             airPollutionProgressColor = Brush.verticalGradient(listOf(Color(0xffD8AD00),Color(0xff624600)))
             airPollutionTextColor = Color(0xffD8AD00)
         }
        3 -> {
            airPollutionTitle = "Unhealthy for Sensitive Groups"
            airPollutionDetail = "Members of sensitive groups may experience health effects. The general public is less likely to be affected."
            airPollutionProgressColor = Brush.verticalGradient(listOf(Color(0xFFD85600),Color(0xFF623400)))
            airPollutionTextColor = Color(0xFFD85600)
        }
        4 -> {
            airPollutionTitle = "Unhealthy"
            airPollutionDetail = "Some members of the general public may experience health effects; members of sensitive groups may experience more serious health effects."
            airPollutionProgressColor = Brush.verticalGradient(listOf(Color(0xffD80000),Color(0xff621000)))
            airPollutionTextColor = Color(0xffD80000)
        }
        5 -> {
            airPollutionTitle = "Very Unhealthy"
            airPollutionDetail = "Health alert: The risk of health effects is increased for everyone."
            airPollutionProgressColor = Brush.verticalGradient(listOf(Color(0xff9F00D8),Color(0xff4F0062)))
            airPollutionTextColor = Color(0xff9F00D8)
        }
        6 -> {
            airPollutionTitle = "Hazardous"
            airPollutionDetail = "Health warning of emergency conditions: everyone is more likely to be affected."
            airPollutionProgressColor = Brush.verticalGradient(listOf(Color(0xFF95002F),Color(0xff4F001C)))
            airPollutionTextColor = Color(0xff95002A)
        }
   }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .glassEffect(hazeState = hazeState)
    ) {
        Text(
            modifier = modifier.fillMaxWidth().padding(top = 12.dp),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.air_quality),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(0.75f)
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
                .background(Brush.verticalGradient(listOf(Color(0xff5C5C5C),Color(0xff424242))), shape = RoundedCornerShape(15.dp))
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
        Spacer(Modifier.height(16.dp))
        Text(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            text = airPollutionDetail,
            textAlign = TextAlign.Center,
            color = airPollutionTextColor,
        )
        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(60.dp, alignment = Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                Text("${airQuality.co.roundToInt()}/m³", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text("CO", color = Color.White.copy(0.5f), style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                Text("${airQuality.o3.roundToInt()}/m³", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text("O3", color = Color.White.copy(0.5f), style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                Text("${airQuality.no2.roundToInt()}/m³", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text("NO2", color = Color.White.copy(0.5f), style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(Modifier.height(32.dp))
        Row(Modifier.fillMaxWidth().padding(bottom = 24.dp), horizontalArrangement = Arrangement.spacedBy(70.dp, alignment = Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                Text("${airQuality.so2.roundToInt()}/m³", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text("SO2", color = Color.White.copy(0.5f), style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                Text("${airQuality.pm25.roundToInt()}/m³", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text("pm2.5", color = Color.White.copy(0.5f), style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                Text("${airQuality.pm10.roundToInt()}/m³", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text("pm10", color = Color.White.copy(0.5f), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
