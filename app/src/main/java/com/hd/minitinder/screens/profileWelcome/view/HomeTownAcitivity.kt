package com.hd.minitinder.screens.profileWelcome.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.ProgressBarStepIndicator
import com.hd.minitinder.common.fragments.button.ButtonGradient
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.register.viewmodel.RegisterViewModel

@Composable
fun HomeTownSceen(navController: NavController, viewModel: RegisterViewModel ) {
    var hometown by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111418))
            .padding(top = 16.dp)
    ) {
        ProgressBarStepIndicator(
            currentStep = 3,
            totalSteps = 6
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF111418))
                .padding(horizontal = 24.dp, vertical = 30.dp),
            verticalArrangement = Arrangement.Top
        ) {

            // Icon đóng (X)
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Close",
                    tint = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .size(24.dp)
                        .clickable(
                            onClick = {
                                navController.popBackStack()
                            }
                        )
                )
            }

            // Tiêu đề
            Text(
                text = "Where do you \ncall home?",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp),
                color = Color.White,
                lineHeight = 40.sp
            )

            // Ô nhập liệu
            TextField(
                value = hometown,
                onValueChange = { hometown = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            // Chú thích nhỏ
            Text(
                text = "This is how it will appear in Tinder.",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
            )

            // Nút Continue
            ButtonGradient(
                buttonText = "Continue",
                onClick = {

                    viewModel.user.value.hometown = hometown
                    navController.navigate(NavigationItem.GenderSelection.route)

                }
            )
        }
    }
}