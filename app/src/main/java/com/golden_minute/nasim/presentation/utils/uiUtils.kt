package com.golden_minute.nasim.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeChild

/**
 * this modifier is used for configuring glass effect on the composable
 */
fun Modifier.glassEffect(hazeState: HazeState) =
    this
        .background(Color.Transparent)
        .hazeChild(
            hazeState,
            RoundedCornerShape(20.dp),
            style = HazeStyle(Color.Black.copy(0.2f), 35.dp, noiseFactor = 0f)
        )
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
            Icon(painter = painterResource(weatherIcon), contentDescription = weatherLabel)
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
                color = Color.White.copy(0.8f)
            )

        } else {
            Text(
                text = weatherLabel,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(0.8f)
            )
            Spacer(modifier.height(10.dp))
            Icon(painter = painterResource(weatherIcon), contentDescription = weatherLabel)
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