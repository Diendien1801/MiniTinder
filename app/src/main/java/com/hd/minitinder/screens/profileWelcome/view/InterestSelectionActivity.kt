package com.hd.minitinder.screens.profileWelcome.view
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.ProgressBarStepIndicator
import com.hd.minitinder.common.fragments.button.ButtonGradient
import com.hd.minitinder.ui.theme.GradientColorsForButton
import com.hd.minitinder.ui.theme.PrimaryColor

@Composable
fun InterestSelectionScreen(nav: NavController) {
    val passions = listOf(
        "'90s kid", "Harry Potter", "SoundCloud", "Spa", "Self-care", "Heavy metal",
        "House parties", "Gin & tonic", "Gymnastics", "Ludo", "Maggi", "Hot yoga",
        "Biryani", "Meditation", "Sushi", "Spotify", "Hockey", "Basketball",
        "Slam poetry", "Home workouts", "Theatre", "Café hopping", "Trainers",
        "Aquarium", "Instagram", "Hot springs", "Walking", "Running", "Travel",
        "Language exchange", "Films", "Guitarists", "Social development", "Gym",
        "Social media", "Hip hop", "Skincare", "J-Pop", "Cricket", "Shisha",
        "Freelance", "K-Pop", "Skateboarding"
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111418))
            .padding(top = 16.dp)
    ){
        ProgressBarStepIndicator(
            currentStep = 6,
            totalSteps = 6
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF111418))
                .padding(24.dp)


        ) {

            // Header
            Icon(
                painter = painterResource(id = R.drawable.ic_back),  // Icon từ drawable
                contentDescription = "Back",
                tint = Color.Gray,
                modifier = Modifier
                    .align(Alignment.Start)
                    .size(24.dp)
                    .clickable(
                        onClick = {
                            nav.popBackStack()
                        }
                    )
            )

            // Title
            Text(
                text = "Passions",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Subtitle
            Text(
                text = "Let everyone know what you're passionate about, by adding it to your profile.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp)
                    .height(560.dp),

                ) {
                passions.forEach { passion ->
                    PassionChip(passion)
                }
            }

            ButtonGradient(
                buttonText = "Continue",
                onClick = {
                    // Xử lý sự kiện khi nút "Next" được nhấn
                },

                )
        }
    }
}

@Composable
fun PassionChip(text: String) {
    var isSelected by remember { mutableStateOf(false) }

    val gradientBrush = Brush.horizontalGradient(
        colors = GradientColorsForButton // Gradient khi chọn
    )

    Box(
        modifier = Modifier
            .padding(3.dp)
            .clickable { isSelected = !isSelected }
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
        ) {
            if (isSelected) {
                drawRoundRect(
                    brush = gradientBrush,
                    size = size,
                    cornerRadius = CornerRadius(20.dp.toPx()),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }

        Box(
            modifier = Modifier
                .border(
                    1.dp,
                    if (isSelected) Color.Transparent else Color.Gray, // Khi không chọn, dùng màu Gray
                    RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                color = if (isSelected) Color.White else Color.Gray
            )
        }
    }
}
