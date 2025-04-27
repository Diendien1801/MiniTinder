package com.hd.minitinder.screens.payment.view

import CheckoutRequest
import DetailChatViewModel
import android.content.Intent
import android.net.Uri
import android.util.Log
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

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.stringResource

import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.hd.minitinder.R
import com.hd.minitinder.navigation.NavigationItem
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hd.minitinder.screens.payment.viewmodel.PaymentViewModel

@Composable
fun TinderGoldOptionScreen(navController: NavHostController) {
    val viewModel = viewModel<PaymentViewModel>()
    var paymentValue by remember { mutableStateOf("100") } // Giá trị payment
    val userId = viewModel.currentUser?.uid
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFE8A5),
                        Color(0xFFFFFFFF)
                    ), // Gradient từ vàng đậm xuống vàng nhạt
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
            text = stringResource(R.string.see_who_likes_you_and_match_with_them_instantly_with_tinder_gold),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.select_a_plan),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        PlanSelection { selectedPrice ->
            paymentValue = selectedPrice
        }

        Spacer(modifier = Modifier.height(16.dp))

        IncludedFeatures()

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { coroutineScope.launch {
                try {
                    val response = RetrofitClient.paymentApi.createCheckoutSession(
                        CheckoutRequest(userId = userId.toString(), price = paymentValue.toInt())
                    )
                    Log.i("url", response.url)
                    val checkoutUrl = response.url

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(checkoutUrl))
                    context.startActivity(intent)
                    navController.navigate(NavigationItem.TinderGold.route)
                } catch (e: Exception) {
                    Log.e("Stripe", "Checkout error: ${e.message}")
                }
            } },
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
                        if (isSelected) Color(0xFFFFC107) else Color.DarkGray, // Yellow border when selected
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
                    Text(text = "$${plan.second}/mth", color = Color.DarkGray)
                }
            }
        }
    }
}

@Composable
fun IncludedFeatures() {
    val features = listOf(
        stringResource(R.string.unlimited_likes),
        stringResource(R.string.see_who_likes_you),
        stringResource(R.string.unlimited_rewinds),
        stringResource(R.string._1_free_boost_per_month),
        stringResource(R.string._5_free_super_likes_per_week),
        stringResource(R.string.passport_match_and_chat_with_people_anywhere_in_the_world),
        stringResource(R.string.control_who_sees_you),
        stringResource(R.string.control_who_you_see),
        stringResource(R.string.hide_ads)
    )

    Column {
        Text(text = stringResource(R.string.included_with_tinder_gold), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        features.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.Green)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = it, color = Color.DarkGray)
            }
        }
    }
}