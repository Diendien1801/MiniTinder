package com.hd.minitinder.screens.authenOption.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.button.ButtonBlackWhite

@Composable
fun AuthenOptionActivity(navController: NavController) {
    val gradientColors = listOf(Color(0xFFFF4458), Color(0xFFFC5B6B))
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, 400f),
                    end = Offset(1000f, 0f)
                )
            )
            .padding(horizontal = 24.dp),

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center) // Căn giữa theo chiều dọc
                .padding(bottom = 100.dp), // Để có khoảng trống ở dưới
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // UI bắt đầu từ giữa màn hình đổ xuống
        ) {
            // Spacer
            Spacer(
                //50% screen
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.3f)
            )
            // Logo Tinder
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo_tinder),
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = "tinder",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(60.dp)) //


            Text(
                text = "By tapping ‘Create account’ or ‘Sign in’ you agree to our Terms. Learn how we process your data in our Privacy Policy and Cookies Policy.",
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp)) // Tạo khoảng cách giữa text và nút
            ButtonBlackWhite (buttonText = "Create account") {
                navController.navigate(com.hd.minitinder.navigation.NavigationItem.Register.route)
            }
            Spacer(modifier = Modifier.height(16.dp))
            ButtonBlackWhite (buttonText = "Sign In") {
                navController.navigate(com.hd.minitinder.navigation.NavigationItem.Login.route)
            }



        }
    }
}
