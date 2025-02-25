package com.hd.minitinder.screens.register.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lint.kotlin.metadata.Visibility
import androidx.navigation.NavController
import com.hd.minitinder.R
import com.hd.minitinder.screens.register.viewmodel.RegisterViewModel
import com.hd.minitinder.navigation.NavigationItem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.hd.minitinder.common.fragments.button.ButtonGradient

@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel = viewModel()) {
    val email by registerViewModel.email
    val password by registerViewModel.password
    val confirmPassword by registerViewModel.confirmPassword
    val primaryColor = Color(0xFFFF4458)
    val gradientColors = listOf(Color(0xFFFF4458), Color(0xFFFC5B6B))

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Phần tiêu đề với nền gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Brush.horizontalGradient(colors = gradientColors)),
            contentAlignment = Alignment.TopStart
        ) {
            Column(horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(36.dp)) {
                Text(
                    text = "Create Your",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = "Account",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

            }
        }

        // Nền trắng với góc bo
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 160.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(36.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { registerViewModel.onEmailChange(it) },
                    placeholder = { Text("ex: abc@cdf.com", color = Color.Gray.copy(alpha = 0.6f)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon", tint = primaryColor)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx() // Độ dày của viền
                            val y = size.height - strokeWidth / 2  // Vẽ ở dưới cùng
                            drawLine(
                                color = primaryColor,
                                start = Offset(36f, y),
                                end = Offset(size.width - 36f, y),
                                strokeWidth = strokeWidth
                            )
                        },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = primaryColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
                )


                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { registerViewModel.onPasswordChange(it) },
                    placeholder = { Text("Password", color = Color.Gray.copy(alpha = 0.6f)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon", tint = primaryColor)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx() // Độ dày của viền
                            val y = size.height - strokeWidth / 2  // Vẽ ở dưới cùng
                            drawLine(
                                color = primaryColor,
                                start = Offset(36f, y),
                                end = Offset(size.width - 36f, y),
                                strokeWidth = strokeWidth
                            )
                        },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = primaryColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
                    placeholder = { Text("Confirm Password", color = Color.Gray.copy(alpha = 0.6f)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Confirm Password Icon", tint = primaryColor)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx() // Độ dày của viền
                            val y = size.height - strokeWidth / 2 // Vẽ ở dưới cùng
                            drawLine(
                                color = primaryColor,
                                start = Offset(36f, y),
                                end = Offset(size.width - 36f, y),
                                strokeWidth = strokeWidth
                            )
                        },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = primaryColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(44.dp))

                // Register Button
                ButtonGradient("Register") {
                    registerViewModel.register()
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Already have an account?",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Login Button
                ButtonGradient("Login") {
                    navController.navigate(NavigationItem.Login.route) {
                        popUpTo(NavigationItem.Register.route) { inclusive = true }
                    }
                }
            }
        }
    }
}

