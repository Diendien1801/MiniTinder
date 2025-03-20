package com.hd.minitinder.screens.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.hd.minitinder.ui.theme.PrimaryColor

// Bottom Sheet cho height và weight
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HWBottomSheet(
    fieldName: String,
    onDismiss: () -> Unit,
) {
    val viewModel: ProfileViewModel = viewModel()
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    // Khởi tạo giá trị cho Height và Weight
    val content = when (fieldName) {
        "Height" -> remember { mutableStateOf(userState.height ?: 0.0) }
        "Weight" -> remember { mutableStateOf(userState.weight ?: 0.0) }
        else -> remember { mutableStateOf(0.0) }
    }

    val range = if (fieldName == "Height") 1..250 else 1..150
    val listState = rememberLazyListState()

    // Đảm bảo cuộn đến giá trị đã chọn
    LaunchedEffect(Unit) {
        val scrollToIndex = if (content.value == 0.0) {
            (150 - range.first)  // Vị trí cuộn nếu giá trị là 0
        } else {
            (content.value - range.first).toInt()
        }
        listState.scrollToItem(scrollToIndex.coerceAtLeast(0))
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        dragHandle = null,
    ) {
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
                    // Cập nhật giá trị cho Height hoặc Weight và lưu vào Firestore
                    when (fieldName) {
                        "Height" -> viewModel.onHeightChange(content.value)
                        "Weight" -> viewModel.onWeightChange(content.value)
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                text = "Select your ${fieldName.lowercase()}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(160.dp)
            ) {
                LazyColumn(state = listState) {
                    items(2) {
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                    items(range.count()) { index ->
                        val value = range.first + index
                        HeightPickerItem(
                            value = value,
                            isSelected = (value == content.value.toInt()),
                            onClick = { content.value = value.toDouble() }
                        )
                    }
                    items(2) {
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .height(1.dp),
                color = Color.LightGray
            )

            Text(
                text = if (fieldName == "Height") "Height unit: cm" else "Weight unit: kg",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .height(1.dp),
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    when (fieldName) {
                        "Height" -> viewModel.onHeightChange(0.0)
                        "Weight" -> viewModel.onWeightChange(0.0)
                    }
                    viewModel.saveUserToFirestore()
                    onDismiss()
                },
                modifier = Modifier
                    .width(120.dp)
                    .height(32.dp)
                    .border(2.dp, Color.LightGray, RoundedCornerShape(24.dp))
                    .padding(0.dp),
            ) {
                Text(
                    text = "Delete ${fieldName.lowercase()}",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun HeightPickerItem(
    value: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val textColor = if (isSelected) PrimaryColor else Color.Black
    val borderColor = if (isSelected) PrimaryColor else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(50)
            )
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = value.toString(), color = textColor)
    }
}
