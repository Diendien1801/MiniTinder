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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.hd.minitinder.R
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.chatList.viewmodel.ChatListViewModel

@Composable
fun ChatListActivity(navController: NavController,chatListViewModel: ChatListViewModel = viewModel()) {


    val listId = chatListViewModel.chatIdList.value
    LaunchedEffect(Unit) {
        chatListViewModel.getChatList()  // Thay "userId_here" bằng userId thực tế
    }
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(start = 16.dp, top = 44.dp, bottom = 16.dp)

        ) {
            Text(
                text = "New Matches",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))

            // New Matches - LazyRow (horizontal scrolling)
            LazyRow {
                items(chatListViewModel.chatList.value) { match ->
                    MatchItem(match)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Messages",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Messages - LazyColumn (vertical scrolling)
            // message hiện tại là ID của người receive
            LazyColumn {
                items(chatListViewModel.chatList.value.zip(chatListViewModel.chatIdList.value)) { (idUser, chatId) ->
                    MessageItem(idUser) {
                        navController.navigate(NavigationItem.DetailChat.createRoute(chatId, idUser))
                    }
                }
            }

        }
    }
}
@Composable
fun MatchItem(name: String) {
    Column(
        modifier = Modifier
            .padding(end = 12.dp)
            .width(80.dp)
            .height(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Căn giữa nội dung theo chiều ngang
    ) {
        Image(
            painter = painterResource(id = R.drawable.avt_temp),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(80.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(6.dp)) // Bo góc 6dp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}



@Composable
fun MessageItem(message: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() } // Xử lý sự kiện khi nhấn
    ) {
        Image(
            painter = painterResource(id = R.drawable.avt_temp),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "John Doe",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
            )
            Text(
                text = message,
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


