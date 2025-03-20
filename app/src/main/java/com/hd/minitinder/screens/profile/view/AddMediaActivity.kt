package com.hd.minitinder.screens.profile.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel

@Composable
fun AddImageScreen(
    navController: NavController
) {
    val viewModel: ProfileViewModel = viewModel()
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    var imageUrls by remember { mutableStateOf<List<String>>(userState.imageUrls) }

    val getImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            if (imageUrls.size < 9) {
                imageUrls = imageUrls + it.toString()
                viewModel.onImageChange(imageUrls)
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Tiêu đề và nút Done
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Add media",
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

            // Giao diện nút Add media
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Hiển thị ảnh dưới dạng lưới (max 9 ảnh)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(9) { index ->
                        if (index < imageUrls.size) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp)
                                    .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                                    .border(1.dp, Color.White, shape = RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                Image(
                                    painter = rememberImagePainter(imageUrls[index]),
                                    contentDescription = "Selected Image",
                                    modifier = Modifier.fillMaxSize(),
                                    alignment = Alignment.Center
                                )
                                IconButton(
                                    onClick = {
                                        imageUrls = imageUrls.filterIndexed { i, _ -> i != index }
                                    },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Color.White, shape = CircleShape)
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Delete"
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp)
                                    .background(
                                        Color.Gray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = { getImageLauncher.launch("image/*") },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color.White, shape = CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Add Media"
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Phần Smart Photos (Text và Switch nằm hai bên)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Smart Photos")
                Switch(
                    checked = true,
                    onCheckedChange = { /* Xử lý sự thay đổi trạng thái Smart Photos */ }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Nút "Add media" dưới cùng
            Button(
                onClick = {
                    navController.navigate(NavigationItem.Profile.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.6f)
                ),
            ) {
                Text("Add media", color = Color.White)
            }
        }
    }
}
