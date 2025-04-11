package com.hd.minitinder.screens.profileWelcome.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.ProgressBarStepIndicator
import com.hd.minitinder.common.fragments.button.ButtonGradient
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.register.viewmodel.RegisterViewModel
import com.hd.minitinder.ui.theme.PrimaryColor
import com.hd.minitinder.utils.Utils


@Composable
fun BirthdayScreen(navController: NavController, viewModel: RegisterViewModel ) {
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    val isButtonEnabled = day.length == 2 && month.length == 2 && year.length == 4

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
    ) {
        ProgressBarStepIndicator(
            currentStep = 2,
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
                text = "Your birthday?",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp, top = 16.dp)
            )

            // Ô nhập ngày sinh
            BirthdayInputSection(
                day = day,
                month = month,
                year = year,
                onDayChange = { day = it },
                onMonthChange = { month = it },
                onYearChange = { year = it },
            )
            // Mô tả
            Text(
                text = "Your profile shows your age, not your birth date.",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
            )
            // Nút "Next"
            ButtonGradient(
                buttonText = "Continue",
                onClick = {
                    viewModel.user.value.dob = Utils.formatDate(day,month,year)
                    navController.navigate(NavigationItem.HomeTown.route)
                },

                )
        }
    }
}

@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 2.dp,
                    color = if (isFocused) PrimaryColor else Color.Gray,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(Color(0xFF22272D))
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .width(60.dp)
                .clickable { isFocused = true },
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = value,
                onValueChange = {
                    if (it.length <= placeholder.length) onValueChange(it)
                },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp, textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.onFocusChanged { isFocused = it.isFocused },
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.Center) {
                        if (value.isEmpty()) {
                            Text(text = placeholder, color = Color.Gray, fontSize = 20.sp)
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
fun BirthdayInputSection(day: String, month: String, year: String, onDayChange: (String) -> Unit, onMonthChange: (String) -> Unit, onYearChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomTextField(value = day, onValueChange = onDayChange, placeholder = "DD")
        Text(text = "/", color = Color.Gray, fontSize = 24.sp, modifier = Modifier.padding(horizontal = 8.dp))
        CustomTextField(value = month, onValueChange = onMonthChange, placeholder = "MM")
        Text(text = "/", color = Color.Gray, fontSize = 24.sp, modifier = Modifier.padding(horizontal = 8.dp))
        CustomTextField(value = year, onValueChange = onYearChange, placeholder = "YYYY")
    }
}