package com.hd.minitinder.screens.profile.view.activity

import android.content.Intent
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale

import com.hd.minitinder.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.hd.minitinder.common.fragments.logo.LogoTinder
import coil.compose.rememberAsyncImagePainter
import com.hd.minitinder.MainActivity
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel

import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.login.viewmodel.LoginViewModel
import com.hd.minitinder.screens.profile.view.ui.theme.LightGray
import com.hd.minitinder.ui.theme.GradientColorsForButton
import com.hd.minitinder.ui.theme.PrimaryColor



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    viewModel: ProfileViewModel = viewModel(),

) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 32.dp)
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    LogoTinder(
                        logoSize = 24.dp,
                        textSize = 30.sp,
                        colorLogo = PrimaryColor,
                        color = PrimaryColor
                    )
                }

                Box(
//                    modifier = Modifier.clickable {
//                        navController.navigate(NavigationItem.History.route)
//                    }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(R.drawable.bell),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(bottom = 16.dp),
                        colorFilter = ColorFilter.tint(Color.Gray) // Áp dụng màu xám
                    )
                }
            }
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
                val painter = if (userState.imageUrls.isNotEmpty()) {
                    rememberAsyncImagePainter(userState.imageUrls.first())
                } else {
                    painterResource(id = R.drawable.ic_launcher_foreground)
                }
                Image(
                    painter = painter,
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
                    color = MaterialTheme.colorScheme.onSurface,
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
                            .background(MaterialTheme.colorScheme.background, shape = CircleShape)
                            .border(1.dp, color = LightGray , shape = CircleShape)
                            .padding(2.dp)
                    ) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color(0xFF939BA7))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Settings",
                        color = LightGray,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
                // Nút sửa profile
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                    IconButton(onClick = { navController.navigate(NavigationItem.EditProfile.route) },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background, shape = CircleShape)
                            .border(1.dp, LightGray, shape = CircleShape)
                            .padding(2.dp)
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit Profile", tint = LightGray)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Edit profile",
                        color = LightGray,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
                // Nút thêm hình ảnh
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton (onClick = {
                        navController.navigate(NavigationItem.AddImage.route)
                    },
                        modifier = Modifier
                            .background(
                                 PrimaryColor,
                                shape = CircleShape
                            )
                            .size(56.dp)
                            .border(1.dp, color = Color.Transparent, shape = CircleShape)
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
                        color = LightGray,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
                //nút logout
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton (onClick = {
                        loginViewModel.logout()
                        // khởi tạo lại main activity
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)

                    },
                        modifier = Modifier
                            .background(
                                PrimaryColor,
                                shape = CircleShape
                            )
                            .size(56.dp)
                            .border(1.dp, color = Color.Transparent, shape = CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Logout,
                            contentDescription = "Logout",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Logout",
                        color = LightGray,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(128.dp))

            // Tinder +
            Text(
                text = "Tinder Platinum",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Level up every action you take on Tinder",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nút tinder +
            Button(
                onClick = { navController.navigate(NavigationItem.TinderGold.route) },
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
