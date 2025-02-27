package com.hd.minitinder.screens.register.view

import android.os.Handler
import android.os.Looper
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

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.R
import com.hd.minitinder.screens.register.viewmodel.RegisterViewModel
import com.hd.minitinder.navigation.NavigationItem

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock

import com.hd.minitinder.common.fragments.button.ButtonGradient
import com.hd.minitinder.common.fragments.edittext.CustomOutlinedTextField
import com.hd.minitinder.common.fragments.popup.SlidingPopup

@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel = viewModel()) {
    val email by registerViewModel.email
    val password by registerViewModel.password
    val confirmPassword by registerViewModel.confirmPassword
    val emailError by registerViewModel.emailError
    val passwordError by registerViewModel.passwordError
    val confirmPasswordError by registerViewModel.confirmPasswordError

    val gradientColors = listOf(Color(0xFFFF4458), Color(0xFFFC5B6B))

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // State điều khiển popup
    var showPopup by remember { mutableStateOf(false) }
    var popupMessage by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Hiển thị SlidingPopup




        // Phần tiêu đề với nền gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Brush.horizontalGradient(colors = gradientColors)),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(36.dp)
            ) {
                Text("Create Your", fontSize = 32.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                Text("Account", fontSize = 32.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
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
                CustomOutlinedTextField(
                    value = email,
                    onValueChange = { registerViewModel.onEmailChange(it) },
                    placeholderText = "ex: abc@cdf.com",
                    leadingIcon = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    errorMessage = emailError
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                CustomOutlinedTextField(
                    value = password,
                    onValueChange = { registerViewModel.onPasswordChange(it) },
                    placeholderText = "Password",
                    leadingIcon = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                    errorMessage = passwordError
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password Field
                CustomOutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
                    placeholderText = "Confirm Password",
                    leadingIcon = Icons.Default.Lock,
                    contentDescription = "Confirm Password Icon",
                    isPassword = true,
                    passwordVisible = confirmPasswordVisible,
                    onPasswordVisibilityToggle = { confirmPasswordVisible = !confirmPasswordVisible },
                    errorMessage = confirmPasswordError
                )

                Spacer(modifier = Modifier.height(44.dp))

                // Register Button
                ButtonGradient("Register",
                    onClick = {
                        registerViewModel.register { success ->
                            if (success) {

                                popupMessage = registerViewModel.errorMessage.value
                                showPopup = true



                                Handler(Looper.getMainLooper()).postDelayed({
                                    navController.navigate(NavigationItem.Login.route) {
                                        popUpTo(NavigationItem.Register.route) { inclusive = true }
                                    }
                                }, 3000) // Delay 3 giây

                            } else {
                                popupMessage = registerViewModel.errorMessage.value
                                showPopup = true
                            }
                        }
                    }
                    )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Already have an account?",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Login Button
                ButtonGradient("Login",
                    onClick = {
                        navController.navigate(NavigationItem.Login.route) {
                            popUpTo(NavigationItem.Register.route) { inclusive = true }
                        }

                })
            }

        }
        Box(
            modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            SlidingPopup(
                message = popupMessage,
                isVisible = showPopup,
                onDismiss = { showPopup = false }
            )
        }
    }
}
