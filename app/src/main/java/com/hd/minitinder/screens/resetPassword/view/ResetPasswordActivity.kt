import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.common.fragments.popup.SlidingPopup
import com.hd.minitinder.screens.resetpassword.viewmodel.ResetPasswordViewModel

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    resetPasswordViewModel: ResetPasswordViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    val isButtonEnabled = email.isNotBlank()
    val gradientColors = listOf(Color(0xFFFD267A), Color(0xFFFF6036))
    var message by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    val isLoading by resetPasswordViewModel.isLoading

    // Trạng thái hiển thị popup
    var isPopupVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Let’s get you back in…",
                fontSize = 26.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(36.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Enter the email associated with your account. We’ll send you a link to help you log back in.",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    resetPasswordViewModel.resetPassword(email) { success, msg ->
                        isSuccess = success
                        message = msg
                        if (success) {
                            isPopupVisible = true
                        }
                    }
                },
                enabled = isButtonEnabled && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.6f)
                ),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(50))
                        .background(
                            brush = if (isButtonEnabled) Brush.horizontalGradient(gradientColors)
                            else Brush.horizontalGradient(
                                listOf(Color(0xFFFD267A), Color(0xFFFF6036))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(text = "Send email", fontSize = 16.sp)
                    }
                }
            }
        }

        // Hiển thị popup khi gửi thành công
        SlidingPopup(
            message = message,
            isVisible = isPopupVisible,
            onDismiss = { isPopupVisible = false }
        )
    }
}
