package com.hd.minitinder.screens.profile.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel
import com.hd.minitinder.ui.theme.PrimaryColor

@Composable
fun PreviewActivity(
    navController: NavController
) {
    val viewModel: ProfileViewModel = viewModel()
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val imageUrls = userState.imageUrls
    val userName = userState.name

    // Vị trí ảnh hiện tại
    var currentIndex by remember { mutableStateOf(0) }
//    LaunchedEffect(imageUrls) {
//        currentIndex = currentIndex.coerceIn(0, if (imageUrls.isNotEmpty()) imageUrls.size - 1 else 0)
//    }

    Scaffold (
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(24.dp))

                // Tiêu đê edit profile và nút done
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Edit profile",
                        color = Color.Black,
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    TextButton(
                        onClick = {
                            navController.navigate(NavigationItem.Profile.route)
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = "Done",
                            color = Color.Blue,
                        )
                    }
                }

                // Nút edit và preview
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(onClick = { navController.navigate(NavigationItem.EditProfile.route) }) {
                            Text(
                                text = "Edit",
                                color = Color.Gray,
                                fontSize = 18.sp
                            )
                        }
                    }

//                HorizontalDivider(
//                    modifier = Modifier
//                        .height(16.dp)
//                        .width(2.dp),
//                    color = Color.Black,
//                )

                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
//                        TextButton(onClick = { navController.navigate(NavigationItem.Preview.route) }) {
                            Text(
                                text = "Preview",
                                color = PrimaryColor,
                                fontSize = 18.sp
                            )
//                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Nếu có ảnh trong danh sách
            if (imageUrls.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = imageUrls[currentIndex],
                    ),
                    contentDescription = "User Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Chia màn hình thành 2 vùng nhấn bằng Row
                Row(modifier = Modifier.fillMaxSize()) {
                    // Vùng nhấn góc trái để lùi lại ảnh trước
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
                    // Vùng nhấn góc phải để chuyển ảnh kế
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
}
