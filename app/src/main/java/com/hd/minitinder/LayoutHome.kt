package com.hd.minitinder.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Lưu trạng thái tab được chọn, mặc định là Home
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    val navItems = listOf(
        NavigationItem.Home to Icons.Default.Home,
        NavigationItem.Profile to Icons.Default.Person
    )

    // Lấy trạng thái điều hướng hiện tại
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Kiểm tra nếu màn hình hiện tại là Login hoặc Register thì ẩn Bottom Navigation
    val shouldShowBottomBar = currentDestination?.route !in listOf(
        NavigationItem.Login.route,
        NavigationItem.Register.route
    )

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.second, contentDescription = item.first.route) },
                            label = { Text(item.first.route) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                navController.navigate(item.first.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
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
            startDestination = NavigationItem.Home.route  // Mặc định vào Home
        )
    }
}
