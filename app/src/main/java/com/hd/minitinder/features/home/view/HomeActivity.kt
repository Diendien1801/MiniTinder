package com.hd.minitinder.features.home.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Screen", style = MaterialTheme.typography.headlineMedium)
        Button(
            onClick = { navController.navigate("LOGIN") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Go to Login")
        }
    }
}
