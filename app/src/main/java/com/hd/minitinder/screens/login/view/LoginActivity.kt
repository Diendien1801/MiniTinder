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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.facebook.CallbackManager
import com.hd.minitinder.R
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
    val gradientColors = listOf(Color(0xFFFD267A), Color(0xFFFF6036))

    // Xử lý khi đăng nhập thành công
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            navController.navigate(NavigationItem.Main.route) {
                popUpTo(NavigationItem.Login.route) { inclusive = true }
            }
        }
    }

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
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Row {
                Image(
                    painter = painterResource(id = R.drawable.logo_tinder),
                    contentDescription = "Logo",
                    modifier = Modifier.size(60.dp).padding(bottom = 16.dp)

                )
                Text(
                    text = "tinder",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Login",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                placeholder = { Text("Email", color = Color.White.copy(alpha = 0.6f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .border(3.dp, Color.White, shape = RoundedCornerShape(50)),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                placeholder = { Text("Password", color = Color.White.copy(alpha = 0.6f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .border(3.dp, Color.White, shape = RoundedCornerShape(50)),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            )


            Text(
                text = "Forgot password?",
                color = Color.White,
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



            Button(
                onClick = { loginViewModel.login() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = primaryColor
                )
            ) {
                Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    activity?.let { loginViewModel.loginWithFacebook(it, callbackManager) }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Login with Facebook", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = primaryColor)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Don't have an account?",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(NavigationItem.Register.route) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Register", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = primaryColor)
            }
        }
    }
}
