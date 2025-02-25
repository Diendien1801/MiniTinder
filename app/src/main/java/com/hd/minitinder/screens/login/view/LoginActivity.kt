package com.hd.minitinder.screens.login.view

import android.app.Activity
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
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.login.viewmodel.LoginViewModel

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

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            navController.navigate(NavigationItem.Main.route) {
                popUpTo(NavigationItem.Login.route) { inclusive = true }
            }
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

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { loginViewModel.onEmailChange(it) },
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
                    onValueChange = { loginViewModel.onPasswordChange(it) },
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
                ButtonGradient("Login") {
                    loginViewModel.login()
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(8.dp, shape = RoundedCornerShape(50)), // Thêm bóng đổ
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // Để dùng background gradient
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues() // Để gradient bao toàn bộ nút
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFFC5B6B), Color(0xFFFF4458)) // Gradient ngang
                                ),
                                shape = RoundedCornerShape(50)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.logo_facebook), // Icon Facebook
                                contentDescription = "Facebook Icon",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Login with Facebook",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Don't have an account?",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Register Button
                ButtonGradient("Register") {
                    // pop login screen
                    navController.navigate(NavigationItem.Register.route) {
                        popUpTo(NavigationItem.Login.route) { inclusive = true }
                    }
                }
            }
        }
    }
}