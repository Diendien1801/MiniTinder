package com.hd.minitinder.screens.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DobBottomSheet(
    onDismiss: () -> Unit
) {
    val viewModel: ProfileViewModel = viewModel()
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    val (day, month, year) = parseDob(userState.dob)
    var selectedDay by remember { mutableIntStateOf(day) }
    var selectedMonth by remember { mutableIntStateOf(month) }
    var selectedYear by remember { mutableIntStateOf(year) }

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
                    val updatedDob = "$selectedDay/$selectedMonth/$selectedYear"
                    viewModel.onDobChange(updatedDob)
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
                text = "Date of birth",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "When is your birthday?",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))


            // Khu vực hiển thị 3 cột chọn Day, Month, Year
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Cột Day
                DobPickerColumn(
                    range = 1..31,
                    selectedValue = selectedDay,
                    onValueSelected = { selectedDay = it }
                )
                // Cột Month
                DobPickerColumn(
                    range = 1..12,
                    selectedValue = selectedMonth,
                    onValueSelected = { selectedMonth = it }
                )
                // Cột Year (ví dụ 1900..2025)
                DobPickerColumn(
                    range = 1900..2025,
                    selectedValue = selectedYear,
                    onValueSelected = { selectedYear = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp),
//            contentAlignment = Alignment.Center // canh giữa theo chiều ngang
//        ) {
//            TextButton(
//                onClick = {
//                    onDayChange(1)
//                    onMonthChange(1)
//                    onYearChange(2000)
//                },
//                modifier = Modifier
//                    .width(120.dp)
//                    .height(32.dp)
//                    .border(2.dp, Color.LightGray, RoundedCornerShape(24.dp))
//                    .padding(0.dp),
//            ) {
//                Text(
//                    text = "Delete Date",
//                    color = Color.Black,
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//        }
        }
    }
}

@Composable
fun DobPickerColumn(
    range: IntRange,
    selectedValue: Int,
    onValueSelected: (Int) -> Unit
) {
    // Tạo LazyListState để quản lý vị trí cuộn
    val listState = rememberLazyListState()

    // Cuộn đến đúng vị trí của ngày/tháng/năm được chọn
    LaunchedEffect(Unit) {
        val index = selectedValue - range.first
        listState.scrollToItem(index)
    }

    Box(
        modifier = Modifier
            .height(150.dp)
            .width(60.dp)
    ) {
        LazyColumn(state = listState) {
            items(range.count()) { index ->
                val value = range.first + index
                DobPickerItem(
                    value = value,
                    isSelected = (value == selectedValue),
                    onClick = { onValueSelected(value) }
                )
            }
        }
    }
}

@Composable
fun DobPickerItem(
    value: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val textColor = Color.Black
    val borderColor = if (isSelected) PrimaryColor else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(50))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = value.toString(), color = textColor)
    }
}

fun parseDob(dob: String?): Triple<Int, Int, Int> {
    // Giá trị mặc định nếu dob rỗng hoặc không đúng định dạng
    val default = Triple(1, 1, 2000)
    if (dob.isNullOrBlank()) return default

    val parts = dob.split("/")
    return if (parts.size == 3) {
        val day = parts[0].toIntOrNull() ?: 1
        val month = parts[1].toIntOrNull() ?: 1
        val year = parts[2].toIntOrNull() ?: 2000
        Triple(day, month, year)
    } else {
        default
    }
}



