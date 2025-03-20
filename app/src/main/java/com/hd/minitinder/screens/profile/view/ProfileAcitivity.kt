package com.hd.minitinder.screens.profile.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale

import com.hd.minitinder.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel

import com.hd.minitinder.navigation.NavigationItem

val gradientColors = listOf(Color(0xFFFF4458), Color(0xFFFC5B6B))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {

    val userState by viewModel.userState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            // Ảnh đại diện
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Add your image resource here
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tên
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = userState.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Verified",
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Các nút
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                // Nút setting
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = { /* Open Settings */ },
                        modifier = Modifier
                            .background(Color.White, shape = CircleShape)
                            .border(1.dp, Color.Black, shape = CircleShape)
                            .padding(2.dp)
                    ) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Settings",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                // Nút sửa profile
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                    IconButton(onClick = { navController.navigate(NavigationItem.EditProfile.route) },
                        modifier = Modifier
                            .background(Color.White, shape = CircleShape)
                            .border(1.dp, Color.Black, shape = CircleShape)
                            .padding(2.dp)
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit Profile")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Edit profile",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                // Nút thêm hình ảnh
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton (onClick = {
                        navController.navigate(NavigationItem.AddImage.route)
                    },
                        modifier = Modifier
                            .background(
                                brush = Brush.radialGradient(
                                    colors = gradientColors,
                                    radius = 50f
                                ),
                                shape = CircleShape
                            )
                            .size(56.dp)
                            .border(1.dp, Color.White, shape = CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Media",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add media",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

            }

            Spacer(modifier = Modifier.height(128.dp))

            // Tinder +
            Text(
                text = "Tinder Platinum",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Level up every action you take on Tinder",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nút tinder +
            Button(
                onClick = { /* Navigate to Tinder Platinum */ },
                colors = ButtonColors(
                    containerColor = Color(0xFFFF4C5C),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFFF4C5C),
                    disabledContentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GET TINDER PLATINUM™")
            }
        }
    }
}
