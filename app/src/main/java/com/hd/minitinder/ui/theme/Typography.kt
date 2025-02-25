package com.hd.minitinder.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hd.minitinder.R

// Định nghĩa FontFamily cho Gotham Rounded
val GothamRounded = FontFamily(
    Font(R.font.chalet_new_york_nineteen_seventy, FontWeight.Normal)
)

// Tạo bộ Typography sử dụng Gotham Rounded
val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = GothamRounded,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = GothamRounded,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = GothamRounded,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    )
)
