package com.hd.minitinder.screens.profile.view.activity

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hd.minitinder.R
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel

@Composable
fun PreviewActivity(
    viewModel: ProfileViewModel
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    val imageUrls = userState.imageUrls
    val userName = userState.name

    // Vị trí ảnh hiện tại
    var currentIndex by remember { mutableStateOf(0) }
    LaunchedEffect(imageUrls) {
        currentIndex = currentIndex.coerceIn(0, if (imageUrls.isNotEmpty()) imageUrls.size - 1 else 0)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Nếu có ảnh trong danh sách
        if (imageUrls.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = imageUrls[currentIndex],
                    error = painterResource(id = R.drawable.ic_launcher_foreground), // nếu có drawable
                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
                ),
                contentDescription = "User Image",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.Fit,

            )

            // Chia màn hình thành 2 vùng nhấn bằng Row
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            if (currentIndex > 0) {
                                currentIndex--
                            }
                        }
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            if (currentIndex < imageUrls.size - 1) {
                                currentIndex++
                            }
                        }
                )
            }

            // Thanh điều hướng ảnh
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                    .fillMaxWidth(0.95f)
                    .height(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (i in imageUrls.indices) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (i == currentIndex)
                                    Color.White
                                else
                                    Color.White.copy(alpha = 0.4f)
                            )
                    )
                }
            }

            // Phần thông tin người dùng ở cuối màn hình
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "$userName",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }


        } else {
            // Trường hợp không có ảnh
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No images to preview")
            }
        }
    }
}
