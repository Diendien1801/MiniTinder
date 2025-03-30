package com.hd.minitinder.common.fragments.button

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hd.minitinder.R
import com.hd.minitinder.ui.theme.GradientColors
import com.hd.minitinder.ui.theme.GradientColorsForButton

@Composable
fun ButtonGradient(
    buttonText: String,
    onClick: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    isLoading: Boolean = false, // ✅ Thêm trạng thái loading
    gradientColors: List<Color> = GradientColorsForButton,
    textColor: Color = Color.White
) {


    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(50)
            ),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = textColor,
            disabledContainerColor = Color.Gray.copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(50))
                .background(brush = Brush.horizontalGradient(gradientColors)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                icon?.let {
                    it()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = buttonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
