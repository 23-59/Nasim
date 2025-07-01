package com.golden_minute.nasim.ui.theme

import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = Color(0xff181818),
    primary = PrimaryGreen,
    surface = Color(0xff1E1E1E),
    onSurface = Color.White.copy(0.75f)

)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun NasimTheme(
    isInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val isInDarkTheme = isInDarkTheme

    MaterialTheme(
        colorScheme = if (isInDarkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,

        ) {
        content()
    }
}