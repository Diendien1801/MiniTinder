package com.hd.minitinder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hd.minitinder.features.home.view.HomeScreen
import com.hd.minitinder.features.login.view.LoginScreen
import com.hd.minitinder.features.register.view.RegisterScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavigationItem.Register.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreen(navController)
        }
        composable(NavigationItem.Register.route) {
            RegisterScreen(navController)
        }
        composable(NavigationItem.Login.route) {
            LoginScreen(navController)
        }
    }
}
