package com.hd.minitinder.common.fragments.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.logo.LogoTinder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SlidingPopup(
    message: String,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White,
                    shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
                .height(80.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_tinder_color),
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp)
            )
            Spacer(
                modifier = Modifier
                    .width(16.dp)
            )
            Text(text = message, color = Color.Red, fontSize = 16.sp)
        }

        LaunchedEffect(isVisible) {
            if (isVisible) {
                coroutineScope.launch {
                    delay(3000) // Hiển thị popup trong 3 giây
                    onDismiss()
                }
            }
        }
    }
}


