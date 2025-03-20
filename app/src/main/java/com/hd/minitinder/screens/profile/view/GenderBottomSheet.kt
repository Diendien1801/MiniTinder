package com.hd.minitinder.screens.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hd.minitinder.ui.theme.PrimaryColor

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderBottomSheet(
    onDismiss: () -> Unit
) {

    val viewModel: ProfileViewModel = viewModel()
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    var content by remember { mutableStateOf(userState.gender) }

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
                    viewModel.onGenderChange(content)
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
            // Phần tiêu đề
            Text(
                text = "Gender",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "What is your gender?",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Hiển thị các lựa chọn giới tính theo dạng oval
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GenderOption(
                    option = "Male",
                    selectedGender = content,
                    onGenderSelected = { newValue -> content = newValue}
                )
                GenderOption(
                    option = "Female",
                    selectedGender = content,
                    onGenderSelected = { newValue -> content = newValue}
                )
                GenderOption(
                    option = "Other",
                    selectedGender = content,
                    onGenderSelected = { newValue -> content = newValue}
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            TextButton(
//                onClick = { onValueChange("") },
//                modifier = Modifier
//                    .width(120.dp)
//                    .height(32.dp)
//                    .border(2.dp, Color.LightGray, RoundedCornerShape(24.dp))
//                    .padding(0.dp),
//            ) {
//                Text(
//                    text = "Delete Gender",
//                    color = Color.Black,
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//        }
    }
    }
}

@Composable
fun GenderOption(
    option: String,
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    val isSelected = option == selectedGender
    Box(
        modifier = Modifier
            .clickable { onGenderSelected(option) }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) PrimaryColor else Color.LightGray,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = option)
    }
}

