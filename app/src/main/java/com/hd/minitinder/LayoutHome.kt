package com.hd.minitinder.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import com.hd.minitinder.R
import com.hd.minitinder.ui.theme.PrimaryColor

@Composable
fun MainScreen(startDestination: String) {
    val navController = rememberNavController()

    // Lưu trạng thái tab được chọn, mặc định là Home
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    val navItems = listOf(
        NavigationItem.Swipe to R.drawable.logo_tinder, // Icon tùy chỉnh từ drawable
        NavigationItem.TinderGold to R.drawable.tinder_gold,
        NavigationItem.Recap to R.drawable.profile,
        NavigationItem.Chat to R.drawable.chat,
        NavigationItem.Profile to R.drawable.profile,
    )


    // Lấy trạng thái điều hướng hiện tại
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Kiểm tra nếu màn hình hiện tại là Login hoặc Register thì ẩn Bottom Navigation
    val shouldShowBottomBar = currentDestination?.route !in listOf(
        NavigationItem.Login.route,
        NavigationItem.Register.route,
        NavigationItem.ResetPass.route,
        NavigationItem.AuthenOption.route,
        NavigationItem.DetailChat.route,
        NavigationItem.ViewProfile.route,
        NavigationItem.Preview.route,
        NavigationItem.AddImage.route,
    )

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar(
                    containerColor = Color.Black,
                    modifier = Modifier.height(80.dp)
                ) {

                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(

                            icon = {
                                Icon(
                                    painter = painterResource(id = item.second),
                                    contentDescription = item.first.route,
                                    tint = if (selectedItem == index) PrimaryColor else Color.Gray,
                                    modifier =  if (index == 1) Modifier.size(30.dp) else Modifier.size(24.dp)
                                )
                            },

                            //label = { Text(item.first.route) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                navController.navigate(item.first.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,  // Màu icon khi chọn
                                unselectedIconColor = Color.Gray, // Màu icon khi chưa chọn
                                selectedTextColor = Color.Black,  // Màu text khi chọn
                                unselectedTextColor = Color.Gray, // Màu text khi chưa chọn
                                indicatorColor = Color.Transparent // Tắt viền (màu nền khi chọn)
                            )

                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        // Đảm bảo khi vào MainScreen, nội dung mặc định là HomeScreen
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
            startDestination = startDestination,// Mặc định vào Home
            callbackManager = CallbackManager.Factory.create()
        )
    }
}