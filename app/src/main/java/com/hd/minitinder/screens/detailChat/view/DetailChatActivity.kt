package com.hd.minitinder.screens.detailChat

import DetailChatViewModel
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.hd.minitinder.R
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.screens.detailChat.components.DateSeparator
import com.hd.minitinder.screens.detailChat.model.ChatMessageModel
import com.hd.minitinder.ui.theme.PrimaryColor
import com.hd.minitinder.utils.Utils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailChatActivity(navController: NavController , chatId: String, receiver: UserModel) {
    val viewModel = viewModel<DetailChatViewModel>()
    val messages by viewModel.messages.collectAsState()
    val inputText = remember { mutableStateOf("") }
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(chatId, receiver.id,context) {
        val currentUserId = viewModel.userId.value // Lấy userId hiện tại
        if (currentUserId != null) {
            viewModel.initChat(chatId, currentUserId, receiver.id, context)
        } else {
            Log.e("DetailChatActivity", "UserId is null!")
        }
    }
    // Tự động cuộn xuống tin nhắn mới nhất
    LaunchedEffect(messages.size) {
        coroutineScope.launch {
            if (messages.isNotEmpty()) {
                listState.scrollToItem(messages.size - 1)
            }
        }
    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.sendImageMessage(context,it)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background( MaterialTheme.colorScheme.background)
            .padding(top = 8.dp)
    ) {
        // ✅ Thanh tiêu đề
        CenterAlignedTopAppBar(
            title = {
                Row(
                    modifier = Modifier
                        .clickable { navController.navigate(com.hd.minitinder.navigation.NavigationItem.ViewProfile.createRoute(receiver.id)) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(receiver.imageUrls[0]),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(receiver.name, fontSize = 16.sp, color =  MaterialTheme.colorScheme.onSurface)
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = PrimaryColor,
                        modifier = Modifier.size(30.dp)
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* TODO: Mở menu */ }, modifier = Modifier.rotate(90f)) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Menu", tint =  MaterialTheme.colorScheme.onSurface)
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor =  MaterialTheme.colorScheme.background,
                titleContentColor =  MaterialTheme.colorScheme.onSurface
            )
        )
        // divider
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray.copy(alpha = 0.6f))
                .height(1.dp)
        )
        // ✅ Danh sách tin nhắn
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            var lastDate: String? = null

            items(messages) { message ->
                val messageDate = Utils.toDateString(message.timestamp)

                if (messageDate != lastDate) {

                    DateSeparator(messageDate)
                    lastDate = messageDate
                }

                ChatBubble(message, isMe = message.senderId == viewModel.getUserId(), receiver)
            }
        }



        // ✅ Thanh nhập tin nhắn
        Box (
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        ){
            MessageInputBar(inputText.value, onTextChange = { inputText.value = it }) {
                coroutineScope.launch {
                    viewModel.sendMessage(context, inputText.value)
                    inputText.value = ""
                }
            }
        }

        // Image icon

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_img),
                contentDescription = "Icon from drawable",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
                    .clickable {
                        imagePickerLauncher.launch("image/*") // Mở bộ chọn ảnh
                    },

                tint =  MaterialTheme.colorScheme.onSurface
            )
        }

    }
}
@Composable
fun ChatBubble(message: ChatMessageModel, isMe: Boolean, receiver: UserModel) {
    Column(

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically

        ) {
            if (!isMe) {
                Image(
                    painter = rememberAsyncImagePainter(receiver.imageUrls[0]),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth(),

                contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
            ) {

                Surface(
                    shape =
                    when{
                        message.type == "image" -> RoundedCornerShape(0.dp)
                        else -> RoundedCornerShape(16.dp)
                    },

                    color = when {
                        message.type == "image" -> Color.Transparent
                        isMe -> PrimaryColor
                        else -> Color.Gray
                    },
                    modifier = Modifier.padding(8.dp)
                )
                {

                    if (message.type == "image") {
                        AsyncImage(
                            model = message.message, // URL hoặc Uri của ảnh
                            contentDescription = "Image message",
                            modifier = Modifier
                                .wrapContentSize()

                        )
                    } else {
                        Text(
                            text = message.message,
                            color = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }


            }



        }
        Text(
            text = Utils.toTimeString(message.timestamp),
            color = Color.Gray,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(), // Giúp căn chỉnh theo textAlign
            fontSize = 12.sp,
            textAlign = if (isMe) TextAlign.End else TextAlign.Start
        )

    }
}
@Composable
fun MessageInputBar(inputText: String, onTextChange: (String) -> Unit, onSend: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(36.dp), // Bo góc cho Surface
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(1.dp, PrimaryColor, RoundedCornerShape(36.dp)) // Đảm bảo viền bo góc
            .clip(RoundedCornerShape(36.dp)) // Bo góc đúng

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = onTextChange,
                placeholder = { Text("Type a message...", color = Color.Gray) },
                singleLine = true, //
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Red
                ),
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent)
            )
            Image(
                painter = painterResource(id = R.drawable.send),
                contentDescription = "SEND",
                modifier = Modifier
                    .padding(start = 8.dp, end = 10.dp)
                    .size(24.dp)
                    .clickable { onSend() }
            )
        }
    }
}

