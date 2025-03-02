import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hd.minitinder.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailChatActivity(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black).padding(top = 8.dp)) {
        // ✅ Sửa lỗi TopAppBar (Dùng CenterAlignedTopAppBar thay vì TopAppBar)
        CenterAlignedTopAppBar(
            title = {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.avt_temp),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text("Hoang Dien", fontSize = 12.sp)


                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Back", tint = Color.Red,
                        modifier = Modifier.size(30.dp)
                        )
                }
            },
            actions = {
                IconButton(onClick = { /* TODO: Xử lý menu */ },modifier = Modifier.rotate(90f)) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Menu", tint = Color.White,

                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Black,
                titleContentColor = Color.White
            )
        )



        // ✅ Hiển thị tin nhắn
        Column(modifier = Modifier
            .weight(1f)
            .padding(horizontal = 12.dp)) {
//            ReceivedMessage("Hey, what's up with dog pics?")
//            Spacer(modifier = Modifier.height(8.dp))
//            SentMessage("cuz em a dog 🥺")
        }

        // ✅ Sửa lỗi TextFieldDefaults.textFieldColors()
        MessageInputBar()
    }
}

@Composable
fun MessageInputBar() {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray.copy(alpha = 0.6f)).height(1.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = "",
                onValueChange = { },
                placeholder = { Text("Type a message...", color = Color.Transparent) },
                colors = TextFieldDefaults.colors( // ✅ Sửa lỗi textFieldColors() thành colors()
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.Transparent,
                    unfocusedTextColor = Color.Transparent,
                    cursorColor = Color.Red,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = { /* TODO: Gửi tin nhắn */ }) {
                Text("SEND", color = Color.Gray)
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray.copy(alpha = 0.6f)).height(1.dp)
        )
        Row {
            IconButton(onClick = { /* TODO: Chọn emoji */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_gif),
                    contentDescription = "Emoji",
                    tint = Color.White
                )
            }
            IconButton(onClick = { /* TODO: Chọn GIF */ }) {
                Icon(painter = painterResource(id = R.drawable.ic_gif), contentDescription = "GIF", tint = Color.White)
            }
            IconButton(onClick = { /* TODO: Spotify */ }) {
                Icon(painter = painterResource(id = R.drawable.ic_gif), contentDescription = "Spotify", tint = Color.White)
            }
        }

    }
}
