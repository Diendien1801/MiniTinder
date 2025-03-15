package com.hd.minitinder.screens.payment.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay

@Composable
fun PaymentQRScreen(navController: NavHostController, payment: String) {
    val totalPayment = payment
    var paymentStatus by remember { mutableStateOf("pending") }
    var timeLeft by remember { mutableIntStateOf(900) }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PAYMENT",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Scan to Pay",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberAsyncImagePainter("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwKuqNI5dhocsYugLdXtrnwjAuRdyTZLvJASg4Hz-f1CFJGTkx5IIAPZoJ7R4KOJ1DZWE&usqp=CAU"),
            contentDescription = "QR Code",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Payment: ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "$$totalPayment",
                fontSize = 18.sp, // Điều chỉnh để phù hợp với dòng
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
        }


        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Time Remaining:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "${"%02d".format(minutes)}:${"%02d".format(seconds)}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Thank you for your support",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Xử lý khi nhấn nút */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Trở lại trang chủ", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
