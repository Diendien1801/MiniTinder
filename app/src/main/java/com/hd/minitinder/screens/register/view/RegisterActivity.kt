package com.hd.minitinder.screens.register.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.R
import com.hd.minitinder.screens.register.viewmodel.RegisterViewModel
import com.hd.minitinder.navigation.NavigationItem

@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel = viewModel()) {
    val email by registerViewModel.email
    val password by registerViewModel.password
    val confirmPassword by registerViewModel.confirmPassword
    val errorMessage by registerViewModel.errorMessage

    val primaryColor = Color(0xFFFF4458)
    val gradientColors = listOf(Color(0xFFFD267A), Color(0xFFFF6036))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background( Brush.linearGradient(
                colors = gradientColors,
                start = Offset(0f, 400f),
                end = Offset(1000f, 0f)
            ))
            .padding(horizontal = 24.dp)

    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Spacer(
                modifier = Modifier.height(80.dp)
            )
            Row {
                Image(
                    painter = painterResource(id = R.drawable.logo_tinder),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "tinder",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Spacer(
                modifier = Modifier.height(60.dp)
            )
            Text(
                text = "Create Account",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Find your match now!",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = { registerViewModel.onEmailChange(it) },
                placeholder = {
                    Text(
                        "Email",
                        color = Color.White.copy(alpha= 0.6f)

                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .background(Color.Transparent)
                    .border(3.dp, Color.White, shape = RoundedCornerShape(50)),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            )





            OutlinedTextField(
                value = password,
                onValueChange = { registerViewModel.onPasswordChange(it) },
                placeholder = {
                    Text(
                        "Password",
                        color = Color.White.copy(alpha= 0.6f)

                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .background(Color.Transparent)
                    .border(3.dp, Color.White, shape = RoundedCornerShape(50)),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
                placeholder = {
                    Text(
                        "Confirm Password",
                        color = Color.White.copy(alpha= 0.6f),


                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .background(Color.Transparent)
                    .border(3.dp, Color.White, shape = RoundedCornerShape(50)),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            )

            Button(
                onClick = { registerViewModel.register() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50), // Bo tròn để giống Tinder hơn
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = primaryColor
                )
            ) {
                Text("Register", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Already have an account?",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Button(
                onClick = { navController.navigate(NavigationItem.Login.route) }
                ,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50), // Bo tròn để giống Tinder hơn
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
            }
        }
    }
}


