package com.hd.minitinder.screens.profileWelcome.view
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.ProgressBarStepIndicator
import com.hd.minitinder.common.fragments.button.ButtonGradient
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.register.viewmodel.RegisterViewModel
import com.hd.minitinder.ui.theme.GradientColorsForButton
import com.hd.minitinder.ui.theme.PrimaryColor

@Composable
fun BioSelectionActivity(nav: NavController,viewModel: RegisterViewModel ) {
    val options = listOf(
        "Long-term partner" to "💘",
        "Long-term, but short-term OK" to "😍",
        "Short-term, but long-term OK" to "🥂",
        "Short-term fun" to "🎉",
        "New friends" to "👋",
        "Still figuring it out" to "🤔"
    )

    var selectedOption by remember { mutableStateOf<String?>(null) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
    ){
        ProgressBarStepIndicator(
            currentStep = 5,
            totalSteps = 6
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)

                .padding(start = 24.dp, end = 24.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {

                // Nút quay lại
                IconButton(
                    onClick = { nav.popBackStack() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Close",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "What are you \nlooking for?",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 40.sp,
                )

                Text(
                    text = "All good if it changes. There's something for everyone.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp, bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(options) { (text, emoji) ->
                        val isSelected = text == selectedOption

                        SelectableBox(
                            text = text,
                            emoji = emoji,
                            isSelected = isSelected,
                            onClick = { selectedOption = text }
                        )

                    }
                }
            }
            ButtonGradient(
                buttonText = "Continue",
                onClick = {
                    viewModel.user.value.bio = selectedOption.toString()
                    nav.navigate(NavigationItem.InterestSelection.route)
                },
                //modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
@Composable
fun SelectableBox(text: String, emoji: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderBrush = if (isSelected) {
        Brush.horizontalGradient(colors = GradientColorsForButton) // Viền gradient khi được chọn
    } else {
        SolidColor(Color.Gray) // Viền xám khi không được chọn
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null) { onClick() }
            .width(50.dp)
            .height(160.dp)
            .padding(2.dp) // Tạo khoảng cách để tránh che mất viền
    ) {
        // Vẽ viền gradient
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val strokeWidth = 3.dp.toPx()
            drawRoundRect(
                brush = borderBrush, // Dùng viền gradient hoặc xám tùy theo trạng thái
                size = size,
                cornerRadius = CornerRadius(16.dp.toPx()),
                style = Stroke(width = strokeWidth)
            )
        }

        // Nội dung bên trong Box
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 24.dp, horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = emoji, fontSize = 24.sp)
                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
