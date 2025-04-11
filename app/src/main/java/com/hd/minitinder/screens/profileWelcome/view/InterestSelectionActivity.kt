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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.ProgressBarStepIndicator
import com.hd.minitinder.common.fragments.button.ButtonGradient
import com.hd.minitinder.common.fragments.popup.SlidingPopup
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.login.viewmodel.LoginViewModel
import com.hd.minitinder.screens.register.viewmodel.RegisterViewModel
import com.hd.minitinder.ui.theme.GradientColorsForButton
import com.hd.minitinder.ui.theme.PrimaryColor
import kotlinx.coroutines.delay

@Composable
fun InterestSelectionScreen(nav: NavController, viewModel: RegisterViewModel ) {
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
    val loginViewModel: LoginViewModel = viewModel()
    val selectedPassions = remember { mutableStateListOf<String>() }
    val isLoading by loginViewModel.isLoading
    val errorMessage by loginViewModel.errorMessage
    var isSuccess by remember { mutableStateOf(false) }
    val loginSuccess by loginViewModel.loginSuccess
    // State điều khiển popup
    var showPopup by remember { mutableStateOf(false) }
    var popupMessage by remember { mutableStateOf("") }
    // Trạng thái hiển thị popup
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            popupMessage = errorMessage
            showPopup = true
        }
    }
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            popupMessage = errorMessage
            delay(1000)
            nav.navigate(NavigationItem.Main.createRoute(NavigationItem.Swipe.route)) {
                popUpTo(NavigationItem.InterestSelection.route) { inclusive = true }
            }

        }
        else
        {
            popupMessage = errorMessage

        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
    ) {
        ProgressBarStepIndicator(currentStep = 6, totalSteps = 6)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.Gray,
                modifier = Modifier
                    .align(Alignment.Start)
                    .size(24.dp)
                    .clickable { nav.popBackStack() }
            )

            Text(
                text = "Passions",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )

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
                    PassionChip(
                        text = passion,
                        isSelected = selectedPassions.contains(passion),
                        onSelectionChanged = { selected ->
                            if (selected) {
                                selectedPassions.add(passion)
                            } else {
                                selectedPassions.remove(passion)
                            }
                        }
                    )
                }
            }

            ButtonGradient(
                buttonText = "Continue",
                onClick = {
                    viewModel.user.value.id = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    viewModel.user.value.interests = selectedPassions.toList() // Lưu sở thích đã chọn
                    viewModel.user.value.imageUrls = listOf("https://github.com/Nhannguyenus24/Thread-clone/blob/main/project/public/image/anonymous-user.jpg?raw=true");
                    viewModel.updateUserModelToDatabase()

                    loginViewModel.email = viewModel.email
                    loginViewModel.password = viewModel.password
                    loginViewModel.login()


                } ,
                isLoading = isLoading
            )
        }
    }
    // Hiển thị popup khi gửi thành công
    SlidingPopup(
        message = popupMessage,
        isVisible = showPopup,
        onDismiss = { showPopup = false }
    )
}

@Composable
fun PassionChip(
    text: String,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    val gradientBrush = Brush.horizontalGradient(
        colors = GradientColorsForButton // Gradient khi chọn
    )

    Box(
        modifier = Modifier
            .padding(3.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null) { onSelectionChanged(!isSelected) }
    ) {
        Canvas(
            modifier = Modifier.matchParentSize()
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
                    if (isSelected) Color.Transparent else Color.Gray,
                    RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Gray
            )
        }
    }
}

