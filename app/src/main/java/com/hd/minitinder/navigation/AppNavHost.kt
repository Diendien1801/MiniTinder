package com.hd.minitinder.navigation

import ResetPasswordScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.facebook.CallbackManager
import com.google.gson.Gson
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.screens.authenOption.view.AuthenOptionActivity
import com.hd.minitinder.screens.chatList.view.ChatListActivity
import com.hd.minitinder.screens.detailChat.DetailChatActivity
import com.hd.minitinder.screens.history.view.HistoryScreen
import com.hd.minitinder.screens.home.view.HomeScreen
import com.hd.minitinder.screens.login.view.LoginScreen
import com.hd.minitinder.screens.payment.view.PaymentQRScreen
import com.hd.minitinder.screens.payment.view.PaymentSuccessScreen
import com.hd.minitinder.screens.payment.view.TinderGoldOptionScreen
import com.hd.minitinder.screens.profile.view.AddImageScreen
import com.hd.minitinder.screens.profile.view.ProfileScreen
import com.hd.minitinder.screens.profile.view.EditProfileScreen
import com.hd.minitinder.screens.profile.view.PreviewActivity
import com.hd.minitinder.screens.register.view.RegisterScreen
import com.hd.minitinder.screens.swipe.view.SwipeScreen
import com.hd.minitinder.screens.tinderGold.view.TinderGoldActivity
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
        composable(NavigationItem.EditProfile.route){
            EditProfileScreen(navController)
        }
        composable(NavigationItem.Preview.route){
            PreviewActivity(navController)
        }
        composable(NavigationItem.AddImage.route){
            AddImageScreen(navController)
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
        composable(
            route = NavigationItem.DetailChat.route,
            arguments = listOf(
                navArgument("chatId") { type = NavType.StringType },
                navArgument("receiverJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            val receiverJson = backStackEntry.arguments?.getString("receiverJson") ?: ""

            // Giải mã URL và chuyển JSON về UserModel
            val decodedJson = URLDecoder.decode(receiverJson, StandardCharsets.UTF_8.toString())
            val receiver: UserModel = Gson().fromJson(decodedJson, UserModel::class.java)

            DetailChatActivity(navController, chatId, receiver)
        }

        composable(NavigationItem.Swipe.route){
            SwipeScreen(navController)
        }
        composable(
            route = NavigationItem.PaymentQR.route,
            arguments = listOf(navArgument("payment") { type = NavType.StringType }) // Khai báo tham số
        ) { backStackEntry ->
            val payment = backStackEntry.arguments?.getString("payment") ?: "0"
            PaymentQRScreen(navController, payment)
        }
        composable(NavigationItem.PaymentSuccess.route) {
            PaymentSuccessScreen(navController)
        }
        composable(NavigationItem.TinderGold.route) {
            TinderGoldActivity(navController)
        }
        composable(NavigationItem.History.route) {
            HistoryScreen(navController)
        }
        composable(NavigationItem.PaymentOption.route) {
            TinderGoldOptionScreen(navController)
        }
    }
}