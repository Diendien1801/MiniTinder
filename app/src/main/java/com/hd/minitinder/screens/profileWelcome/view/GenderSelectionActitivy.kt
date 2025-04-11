package com.hd.minitinder.screens.profileWelcome.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.ProgressBarStepIndicator
import com.hd.minitinder.common.fragments.button.ButtonGradient
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.register.viewmodel.RegisterViewModel
import com.hd.minitinder.ui.theme.GradientColorsForButton
import com.hd.minitinder.ui.theme.PrimaryColor

@Composable
fun GenderSelectionScreen(nav: NavController, viewModel: RegisterViewModel ) {
    var selectedGender by remember { mutableStateOf<String?>(null) }
    var showGender by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
    ){
        ProgressBarStepIndicator(
            currentStep = 4,
            totalSteps = 6
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 30.dp),

            ) {



            // Nút quay lại
            // Icon đóng (X)
            IconButton(
                onClick = { nav.popBackStack() },
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
                text = "What’s your gender?",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Các nút chọn giới tính
            val genders = listOf("Female", "Male", "Other")
            genders.forEach { gender ->
                GenderButton(
                    text = gender,
                    isSelected = selectedGender == gender,
                    onClick = { selectedGender = gender }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Checkbox

            Spacer(modifier = Modifier.weight(1f))

            // Nút Next
            ButtonGradient(
                buttonText = "Continue",
                onClick = {
                    viewModel.user.value.gender = selectedGender.toString()
                    nav.navigate(NavigationItem.BioSelection.route)
                }
            )


        }
    }
}

@Composable
fun GenderButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderBrush = if (isSelected) {
        Brush.horizontalGradient(colors = GradientColorsForButton)
    } else {
        SolidColor(Color.Gray)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        // Vẽ viền
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val strokeWidth = 2.dp.toPx()
            drawRoundRect(
                brush = borderBrush,
                size = size,
                cornerRadius = CornerRadius(50.dp.toPx()),
                style = Stroke(width = strokeWidth)
            )
        }

        // Button bên trong
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.Transparent),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent // Không có màu nền
            ),
            contentPadding = PaddingValues()
        ) {
            Text(text = text, fontSize = 18.sp, color =MaterialTheme.colorScheme.onSurface)
        }
    }
}
