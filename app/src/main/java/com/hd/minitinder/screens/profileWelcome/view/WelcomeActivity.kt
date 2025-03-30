package com.hd.minitinder.screens.profileWelcome.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hd.minitinder.common.fragments.button.ButtonGradient
import com.hd.minitinder.ui.theme.PrimaryColor

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111418))
            .padding(horizontal = 24.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Tinder",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Please follow these house rules",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        RuleItem("Be yourself", "Make sure your photos, age and bio are true to who you are.")
        RuleItem("Stay safe", "Don't be too quick to give out personal information.", linkText = "Date safely")
        RuleItem("Play it cool", "Respect others and treat them as you would like to be treated.")
        RuleItem("Be proactive", "Always report bad behaviour.")

        Spacer(modifier = Modifier.weight(1f))

        // Button Gradient "I Agree"
        ButtonGradient(
            buttonText = "I Agree",
            onClick = {
                navController.navigate(com.hd.minitinder.navigation.NavigationItem.FirstName.route)
            },

        )


    }
}

@Composable
fun RuleItem(title: String, description: String, linkText: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Biểu tượng dấu tick màu đỏ hồng
        Text(
            text = "✔",
            color = PrimaryColor, // Màu đỏ hồng
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 8.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            // Tiêu đề đậm màu trắng
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            // Mô tả màu xám
            Text(
                text = description,
                color = Color(0xFFB0B0B0), // Màu xám nhạt hơn
                fontSize = 14.sp
            )
            // Link (nếu có) màu đỏ hồng
            linkText?.let {
                Text(
                    text = it,
                    color = Color(0xFFFF416C), // Màu đỏ hồng
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

