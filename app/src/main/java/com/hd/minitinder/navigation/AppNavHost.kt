package com.hd.minitinder.navigation

import ResetPasswordScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.hd.minitinder.screens.login.viewmodel.LoginViewModel
import com.hd.minitinder.screens.payment.view.PaymentQRScreen
import com.hd.minitinder.screens.payment.view.PaymentSuccessScreen
import com.hd.minitinder.screens.payment.view.TinderGoldOptionScreen
import com.hd.minitinder.screens.profile.view.activity.AddImageScreen
import com.hd.minitinder.screens.profile.view.activity.EditProfileScreen
import com.hd.minitinder.screens.profile.view.activity.ProfileScreen

import com.hd.minitinder.screens.profileWelcome.view.FirstNameScreen
import com.hd.minitinder.screens.profileWelcome.view.WelcomeScreen
import com.hd.minitinder.screens.recap.view.RecapScreen
import com.hd.minitinder.screens.register.view.RegisterScreen
import com.hd.minitinder.screens.register.viewmodel.RegisterViewModel
import com.hd.minitinder.screens.swipe.view.SwipeScreen
import com.hd.minitinder.screens.tinderGold.view.TinderGoldActivity
import com.hd.minitinder.screens.viewProfile.view.ViewProfileScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import androidx.navigation.navDeepLink


@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    callbackManager: CallbackManager,
    startDestination: String = NavigationItem.AuthenOption.route
) {
    val registerViewModel: RegisterViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreen(navController)
        }
        composable(NavigationItem.Register.route) {
            RegisterScreen(navController,registerViewModel)
        }
        composable(NavigationItem.Login.route) {
            LoginScreen(navController, loginViewModel, callbackManager = callbackManager)
        }
        composable(NavigationItem.Profile.route){
            ProfileScreen(navController,loginViewModel)
        }
        composable(NavigationItem.EditProfile.route){
            EditProfileScreen(navController)
        }

        composable(NavigationItem.AddImage.route){
            AddImageScreen(navController)
        }
        composable(
            route = NavigationItem.Main.route,
            arguments= listOf(
                navArgument("initial"){
                    type = NavType.StringType
                }
            )
        ){
            val startDestination = it.arguments?.getString("initial") ?: NavigationItem.Swipe.route
            MainScreen(startDestination)
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
        composable(
            route = NavigationItem.PaymentSuccess.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "minitinder://payment-success"
                }
            )
        ) {
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

        composable(NavigationItem.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(NavigationItem.FirstName.route) {
            FirstNameScreen(navController,registerViewModel)
        }
        composable(NavigationItem.Birthday.route) {
            com.hd.minitinder.screens.profileWelcome.view.BirthdayScreen(navController,registerViewModel)
        }
        composable (NavigationItem.GenderSelection.route) {
            com.hd.minitinder.screens.profileWelcome.view.GenderSelectionScreen(navController,registerViewModel)
        }
        composable(NavigationItem.HomeTown.route) {
            com.hd.minitinder.screens.profileWelcome.view.HomeTownSceen(navController,registerViewModel)
        }

        composable(NavigationItem.BioSelection.route) {
            com.hd.minitinder.screens.profileWelcome.view.BioSelectionActivity(navController,registerViewModel)
        }

        composable(NavigationItem.InterestSelection.route) {
            com.hd.minitinder.screens.profileWelcome.view.InterestSelectionScreen(navController,registerViewModel)
        }
        composable(NavigationItem.Recap.route) {
            RecapScreen(onBackPressed = { navController.popBackStack() })
        }
        composable("ViewProfile/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ViewProfileScreen(navController = navController, userId = userId)
        }


    }
}