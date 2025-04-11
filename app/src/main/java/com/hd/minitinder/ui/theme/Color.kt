package com.hd.minitinder.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Primary & Accent Colors
val PrimaryColor = Color(0xFFFF4458)
val SecondaryColor = Color(0xFFFF6036)
val GradientColors = listOf(Color(0xFFFF4458), Color(0xFFFC5B6B))
val GradientColorsForButton = listOf(Color(0xFFFD267A), Color(0xFFFF6036))

// Tinder Gold

// Text Colors
val TextColorDark = Color(0xFF000000)
val TextColorLight = Color(0xFFFFFFFF)

// Background Colors
val LightBackgroundColor = Color(0xFFFFFFFF)
val DarkBackgroundColor = Color(0xFF111418)

// Surface Colors
val LightSurface = Color(0xFFC0C0C0)
val DarkSurface = Color(0xFF2C2C2E)    // gần như đen

// Additional Colors
val OnPrimaryLight = Color.White
val OnPrimaryDark = Color(0xFFE0E0E0)

// Light Theme Color Scheme
val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = LightBackgroundColor,
    surface = LightSurface,
    onPrimary = OnPrimaryLight,
    onSecondary = TextColorDark,
    onBackground = TextColorDark,
    onSurface = TextColorDark,

)

// Dark Theme Color Scheme
val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = DarkBackgroundColor,
    surface = DarkSurface,
    onPrimary = OnPrimaryDark,
    onSecondary = TextColorLight,
    onBackground = TextColorLight,
    onSurface = TextColorLight
)

val LightGray = Color(0xFFF0F2F4)
val lightRed = Color