package com.hd.minitinder.screens.chatList.view

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.logo.LogoTinder
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.chatList.viewmodel.ChatListViewModel
import com.hd.minitinder.ui.theme.PrimaryColor

@Composable
fun ChatListActivity(navController: NavController, chatListViewModel: ChatListViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }

    val unreadChats by chatListViewModel.unreadChats

    LaunchedEffect(Unit) {
        chatListViewModel.getChatList()
    }

    val filteredUsers = chatListViewModel.mappedUserList.value.filter { (user, _) ->
        user.name.contains(searchQuery, ignoreCase = true)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 16.dp, top = 20.dp, bottom = 16.dp)
        ) {
            // Logo Tinder
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                LogoTinder(
                    logoSize = 24.dp,
                    textSize = 30.sp,
                    colorLogo = PrimaryColor,
                    color = PrimaryColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text("Search users...", color = Color.Gray)
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedIndicatorColor = Color.Transparent, // bỏ viền mặc định
                    focusedIndicatorColor = Color.Transparent,   // bỏ viền mặc định
                    cursorColor = PrimaryColor,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp)) // ← viền bao trọn TextField
                    .clip(RoundedCornerShape(16.dp))
            )


            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Messages",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nếu đang loading, hiển thị vòng xoay
            if (chatListViewModel.isLoading.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryColor)
                }
            } else {
                // Nếu không loading, hiển thị danh sách chat
                LazyColumn {
                    itemsIndexed(filteredUsers) { index, pair ->
                        MessageItem(pair.first, pair.second) {
                            navController.navigate(NavigationItem.DetailChat.createRoute(chatListViewModel.chatIdList.value[chatListViewModel.chatList.value.indexOf(pair.first.id)], pair.first))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MessageItem(user: UserModel, isUnRead: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() } // Xử lý sự kiện khi nhấn
    ) {
        Box(
            modifier = Modifier.size(80.dp),
            contentAlignment = Alignment.TopEnd // Canh phải trên cùng
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    user.imageUrls.firstOrNull() ?: "https://github.com/Nhannguyenus24/Thread-clone/blob/main/project/public/image/anonymous-user.jpg?raw=true"
                ),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )

            // Hiển thị chấm đỏ nếu tin nhắn chưa đọc
            if (isUnRead) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(PrimaryColor, shape = CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = user.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
            )
            Text(
                text = "New match",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.Gray.copy(alpha = 0.6f))
            )
        }
    }
}



