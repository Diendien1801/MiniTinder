package com.hd.minitinder.navigation

import ResetPasswordScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.facebook.CallbackManager
import com.hd.minitinder.screens.home.view.HomeScreen
import com.hd.minitinder.screens.login.view.LoginScreen
import com.hd.minitinder.screens.profile.view.ProfileScreen
import com.hd.minitinder.screens.register.view.RegisterScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    callbackManager: CallbackManager,
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
            LoginScreen(navController, callbackManager = callbackManager)
        }
        composable(NavigationItem.Profile.route){
            ProfileScreen(navController)
        }
        composable(NavigationItem.Main.route){
            MainScreen()
        }
        composable(NavigationItem.ResetPass.route){
            ResetPasswordScreen(navController)
        }
    }
}
