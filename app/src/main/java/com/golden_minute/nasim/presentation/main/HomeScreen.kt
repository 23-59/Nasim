package com.golden_minute.nasim.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.golden_minute.nasim.R
import com.golden_minute.nasim.presentation.utils.WeatherDetailElement
import com.golden_minute.nasim.presentation.utils.glassEffect
import com.golden_minute.nasim.ui.theme.PrimaryGreen
import com.golden_minute.nasim.ui.theme.nunito
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val hazeState = remember { HazeState() }
    val bottomBarVisible by remember {
        derivedStateOf {
            scrollState.value == scrollState.maxValue || !scrollState.isScrollInProgress
        }
    }
    val hazeStateForSystemBars = remember { HazeState() }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .haze(hazeStateForSystemBars),
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut()
            ) {
                BottomNavigationSection(
                    modifier = modifier.padding(
                        bottom = WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding() + 16.dp,
                        start = 30.dp,
                        end = 30.dp,
                        top = 16.dp,
                    )
                )
            }

        }) { paddingValues ->
        Box(modifier = modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.clear_sky),
                contentDescription = "Background",
                modifier = modifier
                    .fillMaxSize()
                    .haze(hazeState),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(
                        top = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding(),
                        bottom = 150.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                MainWeatherInfoSection(
                    R.drawable.cloud_rain_wind_1.toString(),
                    "Heavy Rain",
                    "27°",
                    "Feels like 10°",
                    "Today 22 Sep 2024",
                    "Tehran, Iran",
                    hazeState,
                    modifier
                )
                DetailSection(
                    modifier = modifier.padding(horizontal = 28.dp),
                    hazeState = hazeState,
                    minTemp = "12°",
                    maxTemp = "17°",
                    windDegree = "120",
                    humidity = "12%",
                    windSpeed = "26.2 m/s",
                    pressure = "964hpa"
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(100.dp)
                            .weight(1f)
                            .padding()
                            .glassEffect(hazeState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sunrise),
                            contentDescription = "sunrise",
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            "06:00",
                            letterSpacing = 3.sp,
                            style = MaterialTheme.typography.titleLarge
                        )
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
                            .heightIn(100.dp)
                            .weight(1f)
                            .glassEffect(hazeState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sun_setting_02),
                            contentDescription = "sunrise",
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            "18:36",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            letterSpacing = 3.sp
                        )
                        Text(
                            "sunset",
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = Color.White.copy(0.5f)
                        )
                    }

                }
                NextHoursForecastSection(
                    modifier,
                    hazeState,
                    listOf(
                        Triple("12°", R.drawable.cloud_02, "12:00"),
                        Triple("18°", R.drawable.sun, "03:00"),
                        Triple("10°", R.drawable.cloud_raining_04, "06:00"),
                        Triple("7°", R.drawable.snowflake_01, "09:00"),
                        Triple("7°", R.drawable.cloud_raining_04, "12:00"),
                        Triple("10°", R.drawable.cloud_raining_04, "03:00"),
                        Triple("9°", R.drawable.cloud_raining_04, "06:00")

                    )
                )
            }
            val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            // glassmorphic status bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(statusBarHeight + 3.dp)
                    .align(Alignment.TopCenter)
                    .background(Color.Transparent)
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
                    .background(Color.Transparent)
                    .hazeChild(hazeStateForSystemBars, style = HazeStyle(noiseFactor = 0f))
            )

        }

    }
}

@Composable
fun BottomNavigationSection(modifier: Modifier = Modifier) {

    var selectedItem by remember { mutableStateOf("Home") }
    val items = listOf(
        "Settings" to R.drawable.settings_02,
        "Locations" to R.drawable.globe_05,
        "Search" to R.drawable.search_lg,
        "Home" to R.drawable.home_05
    )
    val animatedSelectedTabIndex by animateFloatAsState(
        targetValue = items.indexOf(items.find { it.first == selectedItem }).toFloat(),
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
                .clip(RoundedCornerShape(35.dp))
                .height(75.dp)
                .background(
                    brush = Brush.verticalGradient(
                        startY = 0f, endY = 230f, colors =
                        listOf(
                            Color(0xFF414141),
                            Color(0xFF000000)
                        )
                    )
                )
                .border(
                    width = 1.7.dp,
                    shape = RoundedCornerShape(35.dp),
                    brush = Brush.verticalGradient(
                        endY = 130f,
                        colors = listOf(Color.White.copy(0.2f), Color.White.copy(0.1f))
                    )
                )

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
                    selected = selectedItem == key,
                    onClick = { selectedItem = key },
                    icon = { Icon(painter = painterResource(value), contentDescription = key) },
                    label = {
                        Text(
                            key,
                            fontFamily = nunito,
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
                .padding(start = 30.dp, end = 30.dp, top = 16.dp)
                .height(75.dp)
                .clip(
                    RoundedCornerShape(35.dp)
                )
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
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .height(75.dp)

        ) {
            val path = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = size.toRect(),
                        cornerRadius = CornerRadius(size.height)
                    )
                )
            }
            val length = PathMeasure().apply { setPath(path = path, false) }.length
            val tabWidth = size.width / items.size
            drawPath(
                path,
                brush = Brush.horizontalGradient(
                    listOf(
                        PrimaryGreen.copy(0f),
                        PrimaryGreen.copy(1f), PrimaryGreen.copy(1f), PrimaryGreen.copy(0f)
                    ), startX = tabWidth * animatedSelectedTabIndex,
                    endX = tabWidth * (animatedSelectedTabIndex + 1)
                ),
                style = Stroke(
                    7f,
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(
                            length / 2,
                            length
                        )
                    )
                )
            )
        }

    }


}

@Composable
fun DetailSection(
    modifier: Modifier,
    hazeState: HazeState,
    minTemp: String,
    maxTemp: String,
    windDegree: String,
    humidity: String,
    windSpeed: String,
    pressure: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .glassEffect(hazeState = hazeState)

    ) {
        val firstRow = listOf(
            Triple(minTemp, R.drawable.thermometer_cold, "Min Temp"),
            Triple(maxTemp, R.drawable.thermometer_warm, "Max Temp"),
            Triple(windDegree, R.drawable.compass_03, "Wind Degree")
        )
        val secondRow = listOf(
            Triple(humidity, R.drawable.droplets_01, "Humidity"),
            Triple(windSpeed, R.drawable.wind_03, "Wind Speed"),
            Triple(pressure, R.drawable.speedometer_03, "Pressure")
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

@Composable
fun NextHoursForecastSection(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
    nextHoursForecast: List<Triple<String, Int, String>>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .glassEffect(hazeState = hazeState)

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
                    fontFamily = nunito,
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
                WeatherDetailElement(
                    item.second,
                    item.first,
                    item.third,
                    false,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun MainWeatherInfoSection(
    icon: String,
    weatherStatus: String,
    time: String,
    location: String,
    temp:String,
    feelsLike:String,
    hazeState: HazeState,
    modifier: Modifier
) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, top = 16.dp)
            .glassEffect(hazeState)

    ) {
        val (weatherIconPosition, tempTextPosition, locationIconPosition, tempStatusPosition, feelsLikeTextPosition, dateTextPosition, locationTextPosition) = createRefs()

        Text(
            text = time,
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier.constrainAs(dateTextPosition) {
                top.linkTo(parent.top, 12.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        val imagePainter =
            rememberAsyncImagePainter(model = "https://openweathermap.org/img/wn/$icon@2x.png")
        Icon(
            painter = imagePainter,
            contentDescription = "Weather Icon",
            modifier = modifier.constrainAs(weatherIconPosition) {
                top.linkTo(dateTextPosition.top, 5.dp)
                start.linkTo(parent.start, 24.dp)
            })
        Text(
            text = weatherStatus,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = modifier.constrainAs(tempStatusPosition) {
                top.linkTo(weatherIconPosition.bottom, 4.dp)
                start.linkTo(parent.start, 12.dp)
            })
        Icon(
            painter = painterResource(R.drawable.map_marker_outline),
            contentDescription = "Location Icon",
            modifier = modifier.constrainAs(locationIconPosition) {
                top.linkTo(tempStatusPosition.bottom, 4.dp)
                start.linkTo(tempStatusPosition.start)
                bottom.linkTo(parent.bottom, 16.dp)
            })
        Text(
            text =location,
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier.constrainAs(locationTextPosition) {
                top.linkTo(locationIconPosition.top)
                bottom.linkTo(locationIconPosition.bottom)
                start.linkTo(locationIconPosition.end, 4.dp)
            })
        Text(
            text = temp,
            style = MaterialTheme.typography.displayLarge,
            letterSpacing = TextUnit(5f, TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            modifier = modifier.constrainAs(tempTextPosition) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom, 12.dp)
                end.linkTo(parent.end, 26.dp)
            })
        Text(
            text = feelsLike,
            letterSpacing = TextUnit(0.5f, TextUnitType.Sp),
            style = MaterialTheme.typography.titleSmall,
            modifier = modifier.constrainAs(feelsLikeTextPosition) {
                top.linkTo(tempTextPosition.bottom, 4.dp)
                start.linkTo(tempTextPosition.start)
                end.linkTo(tempTextPosition.end, 8.dp)
            })

    }


}