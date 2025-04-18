package com.hd.minitinder.screens.history.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.hd.minitinder.common.fragments.logo.LogoTinder
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.history.viewmodel.HistoryViewModel
import com.hd.minitinder.ui.theme.PrimaryColor

@Composable
fun HistoryScreen(nav: NavController) {
    val viewModel: HistoryViewModel = HistoryViewModel()
    val users by viewModel.filteredUserWithType.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background( MaterialTheme.colorScheme.background) // Set toàn bộ màn hình về đen
    ) {
        Text(
            text = "Activity",
            color =  MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 10.dp),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        HistoryCategorySelector(viewModel)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background( MaterialTheme.colorScheme.background) // Box chứa danh sách cũng có nền đen
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .background( MaterialTheme.colorScheme.background) // LazyColumn có nền đen
            ) {
                items(users) { user ->
                    user.chatId?.let {
                        HistoryItem(
                            user = user.user,
                            time =
                            "11/11/2025",
                            type = user.type,
                            nav = nav,
                            chatId = it,

                            )
                    }
                }
            }
        }
    }
}
@Composable
fun HistoryCategorySelector(viewModel: HistoryViewModel) {
    val selectedCategory by viewModel.selectedCategory.collectAsState() // Lấy trạng thái đã chọn

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val categories = viewModel.categories

        categories.forEach { category ->
            HistoryCategory(
                name = category,
                isSelected = category == selectedCategory,
                onClick = { viewModel.updateCategory(category) } // Gọi hàm cập nhật ViewModel
            )
        }
    }
}


@Composable
fun HistoryCategory(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .height(30.dp)
            .width(70.dp)
            .background( MaterialTheme.colorScheme.background)
            .clickable (interactionSource = remember { MutableInteractionSource() },
                indication = null){ onClick() }, // Bắt sự kiện nhấn

        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, if (isSelected) PrimaryColor else Color.Gray.copy(alpha = 0.6f)), // Viền đổi màu khi chọn
        //color = if (isSelected) PrimaryColor else Color.Transparent // Đổi màu nền khi chọn
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background( MaterialTheme.colorScheme.background)

        ) {
            Text(

                text = name,
                color = if (isSelected) PrimaryColor else Color.Gray.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,

            )
        }
    }
}




@Composable
fun HistoryItem(
    user: UserModel,
    time: String,
    type: HistoryViewModel.NotificationType,
    nav: NavController,
    chatId: String,
) {
    val messageText = when (type) {
        HistoryViewModel.NotificationType.MATCH -> "You matched with ${user.name}. Excited? Good. Now, go say hi."
        HistoryViewModel.NotificationType.LIKE -> "${user.name} liked your profile. Feeling lucky?"
        HistoryViewModel.NotificationType.BLOCK -> "${user.name} has been blocked. You won’t see each other again."
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background( MaterialTheme.colorScheme.background), // Đảm bảo toàn bộ item có màu đen
            .background(Color.Black),
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.background( MaterialTheme.colorScheme.background) // Row chứa thông tin có nền đen
            modifier = Modifier.background(Color.Black)
        ) {
            Surface(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(50.dp),
                shape = CircleShape,
                color =  MaterialTheme.colorScheme.background // Đặt màu cho Surface này để tránh nền bị khác màu
            ) {
                Image(
                    painter = rememberAsyncImagePainter(avtUrl),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Transparent, CircleShape)

                )
            }

            Column(modifier = Modifier.background(Color.Black)) {
                Text(
                    text = messageText,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp
                )

                // Chỉ hiển thị nút nếu không phải Block
                if (type != HistoryViewModel.NotificationType.BLOCK) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(bottom = 20.dp, top = 10.dp),
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(180.dp)
                                .height(40.dp),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, Color.White),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Text(
                                text = "SEND A MESSAGE",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentHeight(Alignment.CenterVertically)
                                    .padding(horizontal = 8.dp)
                                    .clickable()
                                    {
                                        nav.navigate(NavigationItem.DetailChat.createRoute(chatId,user))
                                    }
                            )
                        }
                    }
                }
                //divider
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Gray.copy(alpha = 0.2f))


                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistoryScreen() {
    HistoryScreen(nav = rememberNavController()) // Giả lập NavController
}
