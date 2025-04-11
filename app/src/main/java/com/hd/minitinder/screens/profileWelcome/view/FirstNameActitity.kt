package com.hd.minitinder.screens.profileWelcome.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun FirstNameScreen(navController: NavController, viewModel: RegisterViewModel ) {
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)

    ) {
        ProgressBarStepIndicator(
            currentStep = 1,
            totalSteps = 6
        )
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
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
                    modifier = Modifier.size(24.dp)
                )
            }

            // Tiêu đề
            Text(
                text = "What's your \nfirst name?",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 40.sp
            )

            // Ô nhập liệu
            TextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor =MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
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
                    .weight(1f)
            )

            // Nút Next
            ButtonGradient(
                buttonText = "Continue",
                onClick = {
                    viewModel.user.value.name = name
                    navController.navigate(NavigationItem.Birthday.route)
                }
            )
        }
    }
}
