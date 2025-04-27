package com.hd.minitinder.screens.payment.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hd.minitinder.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PaymentSuccessScreen(navController: NavController) {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, 1)
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val expiryDate = dateFormat.format(calendar.time)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.payment_success),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.thank_you_for_your_support),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.your_tinder_gold_subscription_is_active),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Valid until: $expiryDate",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1976D2) // Xanh nước biển
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* Xử lý điều hướng về trang chủ */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = stringResource(R.string.back_to_homepage), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
