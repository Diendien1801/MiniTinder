package com.hd.minitinder.screens.profile.view.activity

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.hd.minitinder.common.fragments.button.ButtonGradient
import com.hd.minitinder.navigation.NavigationItem

import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel
import com.hd.minitinder.screens.swipe.view.TagChip
import com.hd.minitinder.service.CloudinaryManager
import com.hd.minitinder.ui.theme.PrimaryColor
import com.hd.minitinder.utils.Utils
import kotlinx.coroutines.launch


@Composable
fun AddImageScreen(
    navController: NavController
) {
    val viewModel: ProfileViewModel = viewModel()
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    var imageUrls by remember { mutableStateOf(userState.imageUrls) }
    var selectedTab by remember { mutableStateOf("Edit") }
    LaunchedEffect(userState.imageUrls) {
        imageUrls = userState.imageUrls
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val loadingImages = remember { mutableStateListOf<Uri>() }
    val getImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { selectedUri ->
            if (imageUrls.size < 9) {
                loadingImages.add(selectedUri)
                coroutineScope.launch {
                    val uploadedImageUrl = CloudinaryManager.uploadImageToCloudinary(context, selectedUri)
                    uploadedImageUrl?.let { url ->
                        imageUrls = imageUrls + url
                    }
                    loadingImages.remove(selectedUri)
                }
            }
        }
    }
    // preview screen
    var currentImageIndex by remember { mutableIntStateOf(0) }
    val newImageList = imageUrls + loadingImages.map { it.toString() }
    Scaffold(
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Edit Info",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    TextButton(
                        onClick = {
                            //val newImageList = imageUrls + loadingImages.map { it.toString() }

                            navController.navigate(NavigationItem.Profile.route) {
                                popUpTo(NavigationItem.AddImage.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(text = "Cancel", color = Color.Blue)
                    }
                }
                // Thanh điều hướng Edit - Preview
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    listOf("Edit", "Preview").forEach { tab ->
                        Text(
                            text = tab,
                            color = if (selectedTab == tab) PrimaryColor else MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable { selectedTab = tab }
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (selectedTab == "Edit") {
                Box(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
                {
                    ButtonGradient(
                        buttonText = "Save",
                        onClick = {
                            viewModel.updateImage(newImageList)
                            navController.navigate(NavigationItem.Profile.route) {
                                popUpTo(NavigationItem.AddImage.route) { inclusive = true }
                            }

                        },
                    )
                }
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

            if (selectedTab == "Edit") {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 640.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(9) { index ->
                        when {
                            // Ảnh đã có sẵn
                            index < imageUrls.size -> {
                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(180.dp)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(10.dp)) // Đảm bảo ảnh không bị tràn ra ngoài
                                        .background(Color.Gray),
                                    contentAlignment = Alignment.Center // Căn giữa ảnh trong Box
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(imageUrls[index]),
                                        contentDescription = "Selected Image",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(10.dp)), // Giữ ảnh nằm gọn trong khung bo góc
                                        contentScale = ContentScale.Fit, // Đảm bảo ảnh không bị méo
                                        alignment = Alignment.Center
                                    )

                                    IconButton(
                                        onClick = { imageUrls = imageUrls.filterIndexed { i, _ -> i != index } },
                                        modifier = Modifier
                                            .size(24.dp)
                                            .align(Alignment.BottomEnd) // Đưa nút đóng lên góc trên cùng bên phải
                                            .background(Color.White, shape = CircleShape)
                                            .padding(4.dp)
                                    ) {
                                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close", tint = PrimaryColor)
                                    }
                                }
                            }

                            // Ảnh đang tải lên
                            index - imageUrls.size < loadingImages.size -> {
                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(180.dp)
                                        .padding(4.dp)
                                        .background(
                                            Color.LightGray,
                                            shape = RoundedCornerShape(10.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = PrimaryColor)
                                }
                            }

                            // Ô thêm ảnh mới
                            else -> {
                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(180.dp)
                                        .padding(4.dp)
                                        .background(
                                            Color.Gray.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { getImageLauncher.launch("image/*") },
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    IconButton(
                                        onClick = { getImageLauncher.launch("image/*") },
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(PrimaryColor, shape = CircleShape)
                                            .padding(4.dp)
                                    ) {
                                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Media", tint = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 640.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    //val newImageList = imageUrls + loadingImages.map { it.toString() }

                    // Display current image based on currentImageIndex
                    Image(
                        painter = rememberAsyncImagePainter(newImageList[currentImageIndex]),
                        contentDescription = "${userState.name} - Photo ${currentImageIndex + 1}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Image indicators at the top
                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                            .fillMaxWidth(0.95f)
                            .height(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (i in newImageList.indices) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (i == currentImageIndex)
                                            Color.White
                                        else
                                            Color.White.copy(alpha = 0.4f)
                                    )
                            )
                        }
                    }

                    // Gradient overlay at the bottom for better text visibility
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .align(Alignment.BottomStart)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.6f),
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )

                    // Image navigation controls (centered)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .align(Alignment.Center),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Previous image button
                        IconButton(
                            onClick = {
                                if (currentImageIndex > 0) {
                                    currentImageIndex--
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.3f)),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = "Previous Image",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        // Next image button
                        IconButton(
                            onClick = {
                                if (currentImageIndex < newImageList.size - 1) {
                                    currentImageIndex++
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.3f)),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "Next Image",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    // User info at the bottom
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp, top = 8.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                    ) {
                        // Name and age with shadow
                        Text(
                            text = "${userState.name}, ${Utils.DobToAge(userState.dob)}",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        // Location and occupation
                        Text(
                            text = "${userState.hometown} ",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        // Tags in a flow layout
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            maxItemsInEachRow = Int.MAX_VALUE
                        ) {
                            userState.interests.forEach { interest ->
                                TagChip(tag = interest)
                            }
                        }
                    }
                }
            }
        }
    }
}

