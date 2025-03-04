package com.hd.minitinder.screens.detailChat

import DetailChatViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.R
import com.hd.minitinder.screens.detailChat.model.ChatMessageModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailChatActivity(navController: NavController , chatId: String, receiverId: String) {
    val viewModel = viewModel<DetailChatViewModel>()
    val messages by viewModel.messages.collectAsState()
    val inputText = remember { mutableStateOf("") }

    LaunchedEffect(chatId, receiverId) {
        val currentUserId = viewModel.userId.value // Lấy userId hiện tại
        if (currentUserId != null) {
            viewModel.initChat(chatId, currentUserId, receiverId)
        } else {
            Log.e("DetailChatActivity", "UserId is null!")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 8.dp)
    ) {
        // ✅ Thanh tiêu đề
        CenterAlignedTopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.avt_temp),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hoang Dien", fontSize = 16.sp, color = Color.White)
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.Red,
                        modifier = Modifier.size(30.dp)
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* TODO: Mở menu */ }, modifier = Modifier.rotate(90f)) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Menu", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Black,
                titleContentColor = Color.White
            )
        )

        // ✅ Danh sách tin nhắn
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
                .background(Color.White)
        ) {
            messages.forEach { message ->
                ChatBubble(message, isMe = message.senderId == viewModel.getUserId())
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // ✅ Thanh nhập tin nhắn
        MessageInputBar(inputText.value, onTextChange = { inputText.value = it }) {
            viewModel.sendMessage(inputText.value)
            inputText.value = ""
        }
    }
}
@Composable
fun ChatBubble(message: ChatMessageModel, isMe: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isMe) Color.Blue else Color.Gray,
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = message.message,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
@Composable
fun MessageInputBar(inputText: String, onTextChange: (String) -> Unit, onSend: () -> Unit) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray.copy(alpha = 0.6f))
                .height(1.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = onTextChange,
                placeholder = { Text("Type a message...", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = Color.Red
                ),
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onSend) {
                Text("SEND", color = Color.Gray)
            }
        }
    }
}


