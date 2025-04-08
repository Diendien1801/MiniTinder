package com.hd.minitinder.screens.profile.view.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel
import com.hd.minitinder.ui.theme.PrimaryColor
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestBottomSheet(
    onDismiss: () -> Unit
) {
    val viewModel: ProfileViewModel = viewModel()
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    val interests = listOf(
        "Boating", "Diving", "Jet skiing", "Hiking", "Skydiving", "Backyard gardening",
        "Golf", "Archery", "Skiing", "PlayStation", "Nintendo", "Anime", "Comics",
        "Outdoor activities", "Camping", "Modern dance", "LGBTQA+ rights", "Watching movies",
        "Marvel", "DC", "Harry Potter", "Xbox", "Dungeons & Dragons", "Basketball", "NBA"
    )

    // Initialize selected interests with user's data
//    var selectedInterests by remember { mutableStateOf(userState.interests.toMutableList()) }
    val selectedInterests = remember { mutableStateListOf<String>() }
    LaunchedEffect(userState.interests) {
        selectedInterests.clear() // Đảm bảo rằng danh sách được làm mới trước khi gán giá trị mới
        selectedInterests.addAll(userState.interests) // Gán dữ liệu từ userState vào selectedInterests
    }

    fun toggleInterest(interest: String) {
        if (selectedInterests.contains(interest)) {
            // Nếu đã chọn thì bỏ chọn
            selectedInterests.remove(interest)
        } else {
            // Nếu chưa chọn, kiểm tra số lượng < 5 thì thêm
            if (selectedInterests.size < 5) {
                selectedInterests.add(interest)
            } else {
            }
        }
    }

    // Tạo danh sách sắp xếp lại: Mục nào được chọn thì đưa lên trước
    val sortedInterests = interests.sortedBy { interest -> !selectedInterests.contains(interest) }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            // Title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
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

                // Drag handle at the top center
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

                // Done Button
                TextButton(
                    onClick = {
                        viewModel.onInterestsChange(selectedInterests)
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Interests",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )

                // Hiển thị số lượng đã chọn
                Text(
                    text = "${selectedInterests.size} of 5",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.End
                )
            }

            // Ô tìm kiếm (giả lập)
            TextField(
                value = "",
                onValueChange = { /* Xử lý tìm kiếm */ },
                placeholder = {
                    Row() {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Icon",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = "Search",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(0.dp)
            )
            Spacer(Modifier.height(8.dp))

            FlowRow() {
                sortedInterests.forEach { interest ->
                    val isSelected = selectedInterests.contains(interest)

                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background( Color.White)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) PrimaryColor else Color.Gray,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { toggleInterest(interest) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = interest,
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InterestBubble(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) PrimaryColor else Color.LightGray
    val contentColor = if (isSelected) Color.White else Color.Black

    TextButton(
        onClick = onClick,
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
