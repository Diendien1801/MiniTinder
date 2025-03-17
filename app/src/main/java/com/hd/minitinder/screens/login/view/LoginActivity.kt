package com.hd.minitinder.screens.login.view

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.facebook.CallbackManager
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.button.ButtonGradient
import com.hd.minitinder.common.fragments.edittext.CustomOutlinedTextField
import com.hd.minitinder.common.fragments.popup.SlidingPopup
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.login.viewmodel.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(),
    callbackManager: CallbackManager
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val email by loginViewModel.email
    val password by loginViewModel.password
    val errorMessage by loginViewModel.errorMessage
    val isLoading by loginViewModel.isLoading
    val loginSuccess by loginViewModel.loginSuccess

    val primaryColor = Color(0xFFFF4458)
    val gradientColors = listOf(Color(0xFFFF4458), Color(0xFFFC5B6B))

    var passwordVisible by remember { mutableStateOf(false) }

    // State điều khiển popup
    var showPopup by remember { mutableStateOf(false) }
    var popupMessage by remember { mutableStateOf("") }

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            popupMessage = errorMessage
            showPopup = true
        }
    }
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            popupMessage = errorMessage

            delay(3000)
            navController.navigate(NavigationItem.Main.route) {
                popUpTo(NavigationItem.Login.route) { inclusive = true }
            }

        }
        else
        {
            popupMessage = errorMessage

        }
    }

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
                    text = "Hello",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = "Sign In!",
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

                CustomOutlinedTextField(
                    value = email,
                    onValueChange = { loginViewModel.onEmailChange(it) },
                    placeholderText = "ex: abc@cdf.com",
                    leadingIcon = Icons.Default.Email,
                    contentDescription = "Email Icon",
                )


                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                CustomOutlinedTextField(
                    value = password,
                    onValueChange = { loginViewModel.onPasswordChange(it) },
                    placeholderText = "Password",
                    leadingIcon = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },

                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(Color.Transparent)
                )
                Text(
                    text = "Forgot password?",
                    color = Color.Black,
                    textAlign = TextAlign.End, // Căn phải nội dung văn bản
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth() // Chiếm toàn bộ chiều rộng
                        .padding(bottom = 16.dp)
                        .clickable {
                            navController.navigate(NavigationItem.ResetPass.route)
                        }
                )

                Spacer(modifier = Modifier.height(36.dp))

                // Register Button
                ButtonGradient("Login",
                    onClick = {loginViewModel.login();
                        Log.d("cli","dsadsd")
                    }
                    )
                Spacer(modifier = Modifier.height(16.dp))
                ButtonGradient("" +
                        "Login with Facebook",
                    onClick = {
                        activity?.let { loginViewModel.loginWithFacebook(it, callbackManager) }
                    },
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.logo_facebook),
                            contentDescription = "Facebook Icon",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )


                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Don't have an account?",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Register Button
                ButtonGradient("Register",
                    onClick = {
                        // pop login screen
                        navController.navigate(NavigationItem.Register.route) {
                            popUpTo(NavigationItem.Login.route) { inclusive = true }
                        }
                    }
                )
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