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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.button.ButtonBlackWhite
import com.hd.minitinder.common.fragments.logo.LogoTinder

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
                .align(Alignment.Center)
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Spacer
            Spacer(
                //50% screen
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.3f)
            )
            // Logo Tinder
            LogoTinder()

            Spacer(modifier = Modifier.height(60.dp)) //


            Text(
                text = stringResource(R.string.by_tapping_create_account_or_sign_in_you_agree_to_our_terms_learn_how_we_process_your_data_in_our_privacy_policy_and_cookies_policy),
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp)) // Tạo khoảng cách giữa text và nút
            ButtonBlackWhite (buttonText = stringResource(R.string.create_account)) {
                navController.navigate(com.hd.minitinder.navigation.NavigationItem.Register.route)
            }
            Spacer(modifier = Modifier.height(16.dp))
            ButtonBlackWhite (buttonText = stringResource(R.string.sign_in)) {
                navController.navigate(com.hd.minitinder.navigation.NavigationItem.Login.route)
            }



        }
    }
}
