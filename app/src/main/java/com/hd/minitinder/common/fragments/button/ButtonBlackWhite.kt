package com.hd.minitinder.common.fragments.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ButtonBlackWhite(buttonText: String, onClick: () -> Unit) {
    Button(
    onClick = onClick,
    modifier = Modifier
    .fillMaxWidth()
    .height(50.dp),
    shape = RoundedCornerShape(50),
    colors = ButtonDefaults.buttonColors(
    containerColor = Color.White,
    contentColor = Color.Black
    )
    ) {
        Text(
            text = buttonText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}