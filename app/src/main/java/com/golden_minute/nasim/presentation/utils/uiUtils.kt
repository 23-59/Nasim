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
import androidx.compose.material3.pulltorefresh.PullToRefreshState
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
        .hazeChild(
            hazeState,
            style = HazeStyle(Color.Black.copy(0.2f), blurRadius = 27.dp, noiseFactor = 0f),
            shape = RoundedCornerShape(20.dp)
        )


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
                Color(0xFFC5C5C5).copy(0.7f),
                Color(0xFF5B5B5B),
                Color(0xFFC5C5C5).copy(0.7f),
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

fun getWeatherAppearance(weatherCode: Int, isDay: Int, isOutline: Boolean = false): Int {
    return when (weatherCode) {
        1000 -> {
            if (isDay == 1) {
                if (isOutline) {
                    R.drawable.sun_outline
                } else {
                    R.drawable.sun_main
                }
            } else
                if (isOutline) {
                    R.drawable.moon_outline
                } else {
                    R.drawable.moon
                }
        } // Sunny-Clear
        1003 -> {
            if (isDay == 1){
                if (isOutline)
                    R.drawable.cloudy_day_outline
                 else
                    R.drawable.cloudy_day
            }  else{
                if (isOutline)
                    R.drawable.cloudy_night_outline
                    else
                    R.drawable.cloudy_night
            }
        } // Partly cloudy day-night
        1006 -> {
                if (isOutline)
                    R.drawable.cloud_outline
                    else
                    R.drawable.cloud
        } // Cloudy
        1009 -> {
            if (isOutline)
                R.drawable.cloud_outline
                else
                R.drawable.cloud
        } // Overcast
        1030 -> {
            if (isOutline)
                R.drawable.haze_outline
                else
                R.drawable.haze
        } // Mist
        1063, 1150, 1153, 1180, 1183, 1186, 1189, 1192, 1195, 1240, 1243, 1246 -> {
            if (isOutline)
                R.drawable.rain_outline
                else
                R.drawable.rain_icon
        } // Rain variations
        1066, 1114, 1117, 1210, 1213, 1216, 1219, 1222, 1225, 1255, 1258 -> {
            if (isOutline)
                R.drawable.snow_outline
                else
                R.drawable.snow
        } // Snow variations
        1069, 1072, 1168, 1171, 1198, 1201, 1204, 1207, 1249, 1252, 1261, 1264 -> {
            if (isOutline)
                R.drawable.hail_outline
                else
                R.drawable.hail
        } // Sleet, freezing drizzle, ice pellets
        1087, 1273, 1276, 1279, 1282 -> {
            if (isOutline)
                R.drawable.lightning_and_rain_outline
                else
                R.drawable.lightning_and_rain
        } // Thunder with rain
        1135, 1147 -> {
            if (isOutline)
                R.drawable.haze_outline
                else
                R.drawable.haze
        } // Fog, freezing fog
        1100 -> {
            if (isOutline)
                R.drawable.windy_outline
                else
                R.drawable.windy
        }// Windy
        1089 -> {
            if (isOutline)
                R.drawable.lightning_and_cloud
                else
                R.drawable.lightning_and_cloud
        } // Lightning and cloud
        else -> R.drawable.cloud // Default to cloud icon for unknown codes
    }
}
fun getWeatherAppearance(weatherCode: Int,isDay: Int):String {
    return when (weatherCode) {
        1000 -> {
            if (isDay == 1) {
                "https://unsplash.com/photos/egH5MbQLiMw/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8Nzd8fHN1bnxlbnwwfDF8fHwxNzM0NTY4NjEwfDI&force=true&w=2400"
            } else
                "https://unsplash.com/photos/LSFuPFE9vKE/download?ixid=M3wxMjA3fDB8MXxhbGx8OHx8fHx8fHx8MTczNDU3MjkwM3w&force=true&w=2400"
        } // Sunny-Clear
        1003 -> {
            if (isDay == 1){
                "https://unsplash.com/photos/oalS6SkZc_s/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8MTA0fHxjbG91ZHxlbnwwfDF8fHwxNzM1MzAxMDE3fDI&force=true&w=2400"
            }  else{
                "https://unsplash.com/photos/LluELtL5mK4/download?ixid=M3wxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNzM1MzAxNTY1fA&force=true&w=2400"
            }
        } // Partly cloudy day-night
        1006 -> {
          if (isDay ==1)
              "https://unsplash.com/photos/hYUECF9ZX04/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8NzZ8fGNsb3VkfGVufDB8MXx8fDE3MzUxOTM1NzV8Mg&force=true&w=2400"
            else
              "https://unsplash.com/photos/dWUPJdXiC-M/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8MTk0fHxjbG91ZHxlbnwwfDF8fHwxNzM1MzAxMDY5fDI&force=true&w=2400"
        } // Cloudy
        1009 -> {
            if (isDay ==1)
                "https://unsplash.com/photos/OHzkfrv9Ycw/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8Mzl8fGNsb3VkfGVufDB8MXx8fDE3MzUxOTM0Njd8Mg&force=true&w=2400"
            else
                "https://unsplash.com/photos/dWUPJdXiC-M/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8MTk0fHxjbG91ZHxlbnwwfDF8fHwxNzM1MzAxMDY5fDI&force=true&w=2400"
        } // Overcast
        1030 -> {
           if (isDay ==1)
               "https://unsplash.com/photos/jdM9HbkPhsA/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8MzB8fG1pc3R8ZW58MHwxfHx8MTczNTMwMTg3N3wy&force=true&w=2400"
            else
               "https://unsplash.com/photos/ZSXBILzyvgg/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8MXx8bWlzdCUyMG5pZ2h0fGVufDB8MXx8fDE3MzUzMDE5Njh8Mg&force=true&w=2400"
        } // Mist
        1063, 1150, 1153, 1180, 1183, 1186, 1189, 1192, 1195, 1240, 1243, 1246 -> {
           if (isDay == 1)
               "https://unsplash.com/photos/OlIkdS2oSGw/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8MTA3fHxyYWlufGVufDB8MXx8fDE3MzQ1NzE0ODl8Mg&force=true&w=2400"
            else
               "https://unsplash.com/photos/-G6r58jASE8/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8Mzc2fHxyYWlufGVufDB8MXx8fDE3MzQ0ODYxNDB8Mg&force=true&w=2400"
        } // Rain variations
        1066, 1114, 1117, 1210, 1213, 1216, 1219, 1222, 1225, 1255, 1258 -> {
            if (isDay == 1)
                "https://unsplash.com/photos/OeVpzKVLPJ4/download?ixid=M3wxMjA3fDB8MXxhbGx8NDY1fHx8fHx8fHwxNzM0NTcwNjQyfA&force=true&w=2400"
            else
                "https://unsplash.com/photos/ILLq6afxocQ/download?ixid=M3wxMjA3fDB8MXxhbGx8ODl8fHx8fHx8fDE3MzQ1NzExOTR8&force=true&w=2400"
        } // Snow variations
        1069, 1072, 1168, 1171, 1198, 1201, 1204, 1207, 1249, 1252, 1261, 1264 -> {
            "https://unsplash.com/photos/f6cVTolEhQE/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8Mzd8fGhhaWx8ZW58MHwxfHx8MTczNTE2MTcyOXwy&force=true&w=2400"
        } // Sleet, freezing drizzle, ice pellets
        1087, 1273, 1276, 1279, 1282 -> {
            "https://unsplash.com/photos/rNc2plAblV4/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8MTh8fGxpZ2h0bmluZ3xlbnwwfDF8fHwxNzM1MTYxODYyfDI&force=true&w=2400"
        } // Thunder with rain
        1135, 1147 -> {
            "https://unsplash.com/photos/yY66QAbGxq4/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8MjZ8fGZvZ3xlbnwwfDF8fHwxNzM1MTYyMDYzfDI&force=true&w=2400"
        } // Fog, freezing fog
        1100 -> {
            "https://unsplash.com/photos/DEkbh8NeBh4/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8M3x8d2luZHl8ZW58MHwxfHx8MTczNTE2MjE1OXwy&force=true&w=2400"
        }// Windy
        1089 -> {
            "https://unsplash.com/photos/rNc2plAblV4/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8MTh8fGxpZ2h0bmluZ3xlbnwwfDF8fHwxNzM1MTYxODYyfDI&force=true&w=2400"
        } // Lightning and cloud
        else -> "" // Default to cloud icon for unknown codes
    }
}
