package com.golden_minute.nasim.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.golden_minute.nasim.R

val nunito = FontFamily(Font(R.font.nunito_regular))

// Set of Material typography styles to start with

private val defaultTypography = Typography()
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ), headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = nunito),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = nunito),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = nunito),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = nunito),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = nunito),
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = nunito),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = nunito),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = nunito),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = nunito, letterSpacing = 1.sp),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = nunito),


)