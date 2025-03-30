package com.hd.minitinder.common.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hd.minitinder.ui.theme.PrimaryColor

@Composable
fun ProgressBarStepIndicator(currentStep: Int, totalSteps: Int = 6) {
    val progress = currentStep / totalSteps.toFloat()

    val gradientBrush = Brush.linearGradient(
        colors = listOf( Color(0xFFFF6036),Color(0xFFFD267A),) // Gradient màu
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)

            .background(Color(0xFF333333)) // Màu nền thanh progress
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress) // Fill theo progress
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(gradientBrush) // Áp dụng gradient
        )
    }
}

