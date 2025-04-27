package com.hd.minitinder.screens.viewProfile.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.hd.minitinder.common.fragments.popup.SlidingPopup
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.profile.view.ui.theme.DarkCharcoal
import com.hd.minitinder.screens.profile.view.ui.theme.LightGray
import com.hd.minitinder.screens.viewProfile.viewmodel.OtherProfileViewModel
import com.hd.minitinder.ui.theme.PrimaryColor
import kotlinx.coroutines.launch

@Composable
fun InterestsBox(
    interests: List<String>,
    title: String = "Interests",
    icon: ImageVector = Icons.Default.Favorite
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCharcoal)
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = "$title Icon",
                    tint = Color.LightGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    color = LightGray,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow() {
                interests.forEach { interest ->
                    AssistChip(
                        onClick = { /* Optional: handle click */ },
                        label = { Text(text = interest, color = Color.White) },
                        shape = RoundedCornerShape(50),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color.DarkGray
                        ),
                        modifier = Modifier.padding(8.dp),
                        border = null,
                    )
                }
            }
        }
    }
}

@Composable
fun InfoBox(
    icon: ImageVector,
    title: String,
    content: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCharcoal)
    ) {
        Column {
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = "$title Icon",
                    tint = Color.LightGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    color = LightGray,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = content,
                color = Color.White,
                modifier = Modifier.padding(start = 24.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ShowConfirmDialog(
    title: String,
    description: String,
    confirmText: String = "Confirm",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text(text = confirmText, color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(text = title, fontWeight = FontWeight.Bold)
        },
        text = {
            Text(text = description)
        },
        containerColor = DarkCharcoal,
        titleContentColor = Color.White,
        textContentColor = Color.LightGray
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProfileScreen(navController: NavController, userId: String = "") {
    val viewModel: OtherProfileViewModel = viewModel()

    LaunchedEffect(userId) {
        viewModel.loadUserProfile(userId)
    }
    val userState by viewModel.userProfileState.collectAsStateWithLifecycle()

    userState?.let { user ->
        val imageUrls = user.imageUrls
        var isMatch by remember { mutableStateOf(false) }

        // Kiểm tra "match"
        val currentUserId = viewModel.getCurrentUserId()
        LaunchedEffect(currentUserId, user.id) {
            isMatch = viewModel.isMatch(currentUserId, user.id)
        }

        // Ảnh hiện tại
        var currentIndex by remember { mutableIntStateOf(0) }
        LaunchedEffect(imageUrls) {
            currentIndex = currentIndex.coerceIn(0, if (imageUrls.isNotEmpty()) imageUrls.size - 1 else 0)
        }

        // Thông báo xác nhận
        var showConfirmDialog by remember { mutableStateOf("") }
        when (showConfirmDialog) {
            "Unmatch" -> {
                ShowConfirmDialog(
                    title = "Unmatch this user?",
                    description = "This action is permanent and cannot be undone.",
                    confirmText = "Unmatch",
                    onConfirm = {
                        viewModel.unmatchUser(userId) { success ->
                            if (success) {
                                navController.navigate(NavigationItem.Chat.route)
//                                SlidingPopup(
//                                    message = popupMessage,
//                                    isVisible = showPopup,
//                                    onDismiss = { showPopup = false }
//                                )
                            }
                            else {
                                Log.e("aloalo", "Unmatch failed")
                            }
                        }

                    },
                    onDismiss = { showConfirmDialog = "" }
                )
            }
            "Block" -> {
                ShowConfirmDialog(
                    title = "Block this user?",
                    description = "They will no longer be able to see or interact with you.",
                    confirmText = "Block",
                    onConfirm = {
                        viewModel.blockUser(userId) { success ->
                            if (success) {
                                viewModel.unmatchUser(userId) { success2 ->
                                    if (success2) {
                                        navController.navigate(NavigationItem.Chat.route)
                                    }
                                    else {
                                        Log.e("aloalo", "Block thanh cong nhung chua unmatch")
                                    }
                                }
                            }
                            else {
                                Log.e("aloalo", "Block khong thanh cong")
                            }
                        }
                    },
                    onDismiss = { showConfirmDialog = "" }
                )
            }
            else -> {}
        }

        Scaffold(
            containerColor = Color.Black,
            topBar = {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Box(modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = Color.White,
                        navigationIconContentColor = PrimaryColor
                    )
                )
            }
        ) { innerPadding ->
            val scrollState = rememberScrollState()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Hiển thị ảnh đại diện nếu có
                    if (imageUrls.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(600.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter( model = imageUrls[currentIndex] ),
                                contentDescription = "User Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center),
                                contentScale = ContentScale.FillWidth,
                            )

                            // Chia màn hình thành 2 vùng nhấn bằng Row
                            Row(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clickable { if (currentIndex > 0) currentIndex-- }
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clickable { if (currentIndex < imageUrls.size - 1) { currentIndex++ } }
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
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.name.take(1),
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        InfoBox(
                            icon = Icons.Default.FormatQuote,
                            title = "About me",
                            content = user.bio
                        )

                        InfoBox(
                            icon = Icons.Default.Person,
                            title = "Gender",
                            content = user.gender
                        )

                        InfoBox(
                            icon = Icons.Default.Cake,
                            title = "Date of Birth",
                            content = user.dob
                        )

                        InfoBox(
                            icon = Icons.Default.Place,
                            title = "Hometown",
                            content = user.hometown
                        )

                        InfoBox(
                            icon = Icons.Default.Work,
                            title = "Job",
                            content = user.job
                        )

                        InterestsBox(interests = user.interests)

                        InfoBox(
                            icon = Icons.Default.Height,
                            title = "Height",
                            content = "${user.height} cm"
                        )

                        InfoBox(
                            icon = Icons.Default.MonitorWeight,
                            title = "Weight",
                            content = "${user.weight} kg"
                        )

                        InfoBox(
                            icon = Icons.Default.Phone,
                            title = "Phone",
                            content = user.phoneNumber
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isMatch){
                        Button(
                            onClick = {
                                showConfirmDialog = "Unmatch"
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF21262E),
                                contentColor = Color.White,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .clip(RoundedCornerShape(0.5.dp))
                        ) {
                            Text(
                                text = "Unmatched",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            showConfirmDialog = "Block"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF21262E),
                            contentColor = Color.Red,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .clip(RoundedCornerShape(0.5.dp))
                    ) {
                        Text(
                            text = "Block",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    } ?: run {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
