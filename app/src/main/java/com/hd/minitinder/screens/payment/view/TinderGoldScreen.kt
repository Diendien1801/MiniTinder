package com.hd.minitinder.screens.payment.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.hd.minitinder.navigation.NavigationItem

@Composable
fun TinderGoldScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1))
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { /* Handle close action */ },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "See who Likes You and match with them instantly with Tinder Gold™.",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select a plan",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        PlanSelection()

        Spacer(modifier = Modifier.height(16.dp))

        IncludedFeatures()

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(
                    onClick = { navController.navigate(NavigationItem.PaymentQR.route) }
                ))
        }

    }
}

@Composable
fun PlanSelection() {
    val plans = listOf(
        "1 month" to "₹573.21/mth",
        "6 months" to "₹368.83/mth"
    )
    var selectedPlan by remember { mutableStateOf(plans[0]) }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        plans.forEach { plan ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .border(
                        2.dp,
                        if (plan == selectedPlan) Color.Black else Color.Gray,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { selectedPlan = plan }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = plan.first, fontWeight = FontWeight.Bold)
                    Text(text = plan.second, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun IncludedFeatures() {
    val features = listOf(
        "Unlimited likes",
        "See who Likes You",
        "Unlimited Rewinds",
        "1 free Boost per month",
        "5 free Super Likes per week",
        "Passport™ - Match and chat with people anywhere in the world",
        "Control who sees you",
        "Control who you see",
        "Hide ads"
    )

    Column {
        Text(text = "Included with Tinder Gold™", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        features.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.Green)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = it)
            }
        }
    }
}