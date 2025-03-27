package com.hd.minitinder.screens.profile.view

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.profile.model.uploadImageToFirebase
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel
import com.hd.minitinder.ui.theme.PrimaryColor

@Composable
fun AddImageScreen(
    navController: NavController
) {
    val viewModel: ProfileViewModel = viewModel()
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    var imageUrls by remember { mutableStateOf(userState.imageUrls) }
    LaunchedEffect(userState.imageUrls) {
        imageUrls = userState.imageUrls
    }
    val getImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            if (imageUrls.size < 9) {
                uploadImageToFirebase(it,
                    onSuccess = { downloadUrl ->
                        // Sau khi có URL, cập nhật danh sách URL và lưu lên database
                        imageUrls = imageUrls + downloadUrl
                        viewModel.onImageChange(imageUrls)
                        Log.d("aloalo",downloadUrl)
                    },
                    onFailure = { exception ->
                        Log.d("aloalo","aloalo")
                        // Xử lý lỗi nếu upload thất bại
                        // Ví dụ: hiển thị thông báo lỗi cho người dùng
                    }
                )
            }
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Add media",
                    color = Color.Black,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                TextButton(
                    onClick = { navController.navigate(NavigationItem
                        .Profile.route) },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(text = "Done", color = Color.Blue)
                }
            }
        },
        bottomBar = {
            Button(
                onClick = { navController.navigate(NavigationItem.Profile.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.6f)
                )
            ) {
                Text("Add media", color = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị ảnh dưới dạng lưới (max 9 ảnh)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 640.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(9) { index ->
                    if (index < imageUrls.size) {
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(180.dp)
                                .padding(4.dp)
                                .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                                .clickable {
                                    imageUrls = imageUrls.filterIndexed { i, _ -> i != index }
                                },
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUrls[index]),
                                contentDescription = "Selected Image",
                                modifier = Modifier.fillMaxSize(),
                                alignment = Alignment.Center
                            )
                            IconButton(
                                onClick = { imageUrls = imageUrls.filterIndexed { i, _ -> i != index } },
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(color = Color.White, shape = CircleShape)
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = PrimaryColor
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(180.dp)
                                .padding(4.dp)
                                .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(10.dp))
                                .clickable {
                                    getImageLauncher.launch("image/*")
                                },
                            contentAlignment = Alignment.BottomEnd

                        ) {
                            IconButton(
                                onClick = { getImageLauncher.launch("image/*") },
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(PrimaryColor, shape = CircleShape)
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add Media",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Phần Smart Photos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(text = "Smart Photos")
                Switch(
                    checked = true,
                    onCheckedChange = { /* Xử lý sự thay đổi trạng thái Smart Photos */ },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,       // Màu của nút khi bật
                        checkedTrackColor = PrimaryColor,        // Màu nền của switch khi bật
                        uncheckedThumbColor = Color.White,       // Màu của nút khi tắt
                        uncheckedTrackColor = Color.LightGray    // Màu nền của switch khi tắt
                )
                )
            }
        }
    }
}
