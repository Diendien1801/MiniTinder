package com.hd.minitinder.screens.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameBottomSheet(
    fieldName: String,
    onDismiss: () -> Unit,
) {
    val viewModel: ProfileViewModel = viewModel()
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    val description = when (fieldName) {
        "Name" -> "Let us know your name."
        "Hometown" -> "Where do you come from?"
        "Job" -> "What is your profession?"
        "Phone number" -> "What is your phone number?"
        else -> "Please provide your $fieldName"
    }

    val content = when (fieldName) {
        "Name" -> remember { mutableStateOf(userState.name) }
        "Hometown" -> remember { mutableStateOf(userState.hometown) }
        "Job" -> remember { mutableStateOf(userState.job) }
        "Phone number" -> remember { mutableStateOf(userState.phoneNumber) }
        else -> remember { mutableStateOf("") }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        dragHandle = null,
    ) {
        // Các phần của giao diện bottom sheet
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            // Nút hủy bỏ ở góc trái trên
            IconButton(
                onClick = { onDismiss() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 8.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = Color.Gray
                )
            }

            // Drag handle ở giữa trên (top center)
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 4.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .background(color = Color.LightGray, shape = RoundedCornerShape(2.dp))
            )

            // Nút hoàn thành ở góc phải trên
            TextButton(
                onClick = {
                    when (fieldName) {
                        "Name" -> viewModel.onNameChange(content.value)
                        "Hometown" -> viewModel.onHometownChange(content.value)
                        "Job" -> viewModel.onJobChange(content.value)
                        "Phone number" -> viewModel.onPhoneNumberChange(content.value)
                    }
                    viewModel.saveUserToFirestore()
                    onDismiss()
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
            ) {
                Text("Done")
            }
        }

        // Nội dung chính
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = fieldName,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = content.value,
                onValueChange = { newValue -> content.value = newValue },
                label = { Text(fieldName) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
