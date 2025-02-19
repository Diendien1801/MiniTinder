package com.hd.minitinder.features.register.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.features.register.viewmodel.RegisterViewModel
import com.hd.minitinder.navigation.NavigationItem


@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel = viewModel()) {
    val email by registerViewModel.email
    val password by registerViewModel.password
    val confirmPassword by registerViewModel.confirmPassword
    val errorMessage by registerViewModel.errorMessage

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,

                text = "Create new account",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Please enter your details",
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Text(text = "Your email")
            OutlinedTextField(
                value = email,
                onValueChange = { registerViewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(text = "Your password", modifier = Modifier.padding(top = 8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { registerViewModel.onPasswordChange(it) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(text = "Your confirm password", modifier = Modifier.padding(top = 8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
                label = { Text("Confirm password") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { registerViewModel.register() },
                modifier = Modifier.width(400.dp)
            ) {
                Text("Register")
            }

            Text(text = "Already have an account?")

            Button(
                onClick = { navController.navigate(NavigationItem.Login.route) },
                modifier = Modifier.width(400.dp)
            ) {
                Text("Login")
            }
        }
    }
}


