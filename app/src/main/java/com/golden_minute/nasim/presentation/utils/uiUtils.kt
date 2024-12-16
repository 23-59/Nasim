package com.golden_minute.nasim.presentation.utils

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.golden_minute.nasim.R
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeChild


/**
 * this modifier is used for configuring glass effect on the composable
 */
fun Modifier.glassEffect(hazeState: HazeState) =
    this
        .clip(RoundedCornerShape(20.dp))
        .border(
            shape = RoundedCornerShape(20.dp),
            width = 2.dp,
            brush = Brush.verticalGradient(
                listOf(
                    Color.White.copy(0.4f),
                    Color.White.copy(0.2f)
                )
            )
        )
        .hazeChild(hazeState, style = HazeStyle(Color.Black.copy(0.2f), blurRadius = 30.dp, noiseFactor = 0f), shape = RoundedCornerShape(20.dp))


fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2f * size.width.toFloat(),
        targetValue = 2f * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "shimmerEffect",
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFC5C5C5).copy(0.5f),
                Color(0xFF6E6E6E),
                Color(0xFFC5C5C5).copy(0.5f),
            ), start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())

        )
    ).onGloballyPositioned {
        size = it.size
    }
}


@Composable
fun WeatherDetailElement(
    weatherIcon: Int,
    weatherStatus: String,
    weatherLabel: String,
    iconAtTop: Boolean,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        if (iconAtTop) {
            Icon(painter = painterResource(weatherIcon), contentDescription = weatherLabel, modifier = modifier.size(40.dp))
            Spacer(modifier.height(10.dp))
            Text(
                text = weatherStatus,
                style = MaterialTheme.typography.headlineSmall,
                letterSpacing = 1.sp
            )
            Spacer(modifier.height(10.dp))
            Text(
                text = weatherLabel,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(0.6f)
            )

        } else {
            Text(
                text = weatherLabel,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(0.8f)
            )
            Spacer(modifier.height(10.dp))
            Image(
                painter = painterResource(weatherIcon),
                contentDescription = weatherLabel,
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier.height(10.dp))
            Text(
                text = weatherStatus,
                style = MaterialTheme.typography.titleLarge,
                letterSpacing = 2.sp
            )
            Spacer(modifier.height(16.dp))

        }

    }
}

fun getWeatherIcon(weatherCode: Int, isDay: Int, isOutline: Boolean? = null): Int {
    return when (weatherCode) {
        1000 -> {
            if (isDay == 1) {
                if (isOutline == true) {
                    R.drawable.sun_outline
                } else {
                    R.drawable.sun_main
                }
            } else
                if (isOutline == true) {
                    R.drawable.moon_outline
                } else {
                    R.drawable.moon
                }
        } // Sunny-Clear
        1003 -> {
            if (isDay == 1){
                if (isOutline == true)
                    R.drawable.cloudy_day_outline
                 else
                    R.drawable.cloudy_day
            }  else{
                if (isOutline == true)
                    R.drawable.cloudy_night_outline
                    else
                    R.drawable.cloudy_night
            }
        } // Partly cloudy day-night
        1006 -> {
                if (isOutline== true)
                    R.drawable.cloud_outline
                    else
                    R.drawable.cloud
        } // Cloudy
        1009 -> {
            if (isOutline==true)
                R.drawable.cloud_outline
                else
                R.drawable.cloud
        } // Overcast
        1030 -> {
            if (isOutline==true)
                R.drawable.haze_outline
                else
                R.drawable.haze
        } // Mist
        1063, 1150, 1153, 1180, 1183, 1186, 1189, 1192, 1195, 1240, 1243, 1246 -> {
            if (isOutline==true)
                R.drawable.rain_outline
                else
                R.drawable.rain_icon
        } // Rain variations
        1066, 1114, 1117, 1210, 1213, 1216, 1219, 1222, 1225, 1255, 1258 -> {
            if (isOutline==true)
                R.drawable.snow_outline
                else
                R.drawable.snow
        } // Snow variations
        1069, 1072, 1168, 1171, 1198, 1201, 1204, 1207, 1249, 1252, 1261, 1264 -> {
            if (isOutline ==true)
                R.drawable.hail_outline
                else
                R.drawable.hail
        } // Sleet, freezing drizzle, ice pellets
        1087, 1273, 1276, 1279, 1282 -> {
            if (isOutline==true)
                R.drawable.lightning_and_rain_outline
                else
                R.drawable.lightning_and_rain
        } // Thunder with rain
        1135, 1147 -> {
            if (isOutline==true)
                R.drawable.haze_outline
                else
                R.drawable.haze
        } // Fog, freezing fog
        1100 -> {
            if (isOutline == true)
                R.drawable.windy_outline
                else
                R.drawable.windy
        }// Windy
        1089 -> {
            if (isOutline == true)
                R.drawable.lightning_and_cloud
                else
                R.drawable.lightning_and_cloud
        } // Lightning and cloud
        else -> R.drawable.cloud // Default to cloud icon for unknown codes
    }
}
