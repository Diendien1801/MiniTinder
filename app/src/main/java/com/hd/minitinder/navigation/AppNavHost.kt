package com.hd.minitinder.navigation

import DetailChatActivity
import ResetPasswordScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.facebook.CallbackManager
import com.hd.minitinder.screens.authenOption.view.AuthenOptionActivity
import com.hd.minitinder.screens.chatList.view.ChatListActivity
import com.hd.minitinder.screens.home.view.HomeScreen
import com.hd.minitinder.screens.login.view.LoginScreen
import com.hd.minitinder.screens.payment.view.PaymentQRScreen
import com.hd.minitinder.screens.payment.view.PaymentSuccessScreen
import com.hd.minitinder.screens.payment.view.TinderGoldScreen
import com.hd.minitinder.screens.profile.view.ProfileScreen
import com.hd.minitinder.screens.register.view.RegisterScreen
import com.hd.minitinder.screens.swipe.view.SwipeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    callbackManager: CallbackManager,
    startDestination: String = NavigationItem.AuthenOption.route
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
        composable(NavigationItem.AuthenOption.route) {
            AuthenOptionActivity(navController)
        }
        composable(NavigationItem.Chat.route){
            ChatListActivity(navController)
        }
        composable(NavigationItem.DetailChat.route){
            DetailChatActivity(navController)
        }
        composable(NavigationItem.Swipe.route){
            SwipeScreen(navController)
        }
        composable(NavigationItem.PaymentQR.route){
            PaymentQRScreen(navController)
        }
        composable(NavigationItem.PaymentSuccess.route) {
            PaymentSuccessScreen(navController)
        }
        composable(NavigationItem.TinderGold.route) {
            TinderGoldScreen(navController)

        }
    }
}