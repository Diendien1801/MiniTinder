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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.hd.minitinder.navigation.NavigationItem

@Composable
fun TinderGoldOptionScreen(navController: NavHostController) {
    var paymentValue by remember { mutableStateOf("100") } // Giá trị payment

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFE8A5), Color(0xFFFFFFFF)), // Gradient từ vàng đậm xuống vàng nhạt
                    startY = 0f,
                    endY = 200f
                )
            )

            .padding(16.dp)
    ) {
        IconButton(
            onClick = { navController.navigate(NavigationItem.PaymentQR.createRoute("1000")) },
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

        // Truyền callback để cập nhật giá trị payment
        PlanSelection { selectedPrice ->
            paymentValue = selectedPrice
        }

        Spacer(modifier = Modifier.height(16.dp))

        IncludedFeatures()

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("paymentQR/$paymentValue") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

    }
}

@Composable
fun PlanSelection(onPlanSelected: (String) -> Unit) {
    val plans = listOf(
        "1 month" to "100",
        "6 months" to "60"
    )
    var selectedPlan by remember { mutableStateOf(plans[0]) }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        plans.forEach { plan ->
            val isSelected = plan == selectedPlan
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .border(
                        2.dp,
                        if (isSelected) Color(0xFFFFC107) else Color.Gray, // Yellow border when selected
                        RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        selectedPlan = plan
                        val finalValue = if (plan.first == "6 months") {
                            (plan.second.toInt() * 6).toString() // Multiply by 6
                        } else {
                            plan.second
                        }
                        onPlanSelected(finalValue)
                    }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = plan.first, fontWeight = FontWeight.Bold)
                        if (isSelected) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color.Green,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(text = "$${plan.second}/mth", color = Color.Gray)
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