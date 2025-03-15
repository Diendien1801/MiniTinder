package com.hd.minitinder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hd.minitinder.screens.home.view.HomeScreen
import com.hd.minitinder.screens.login.view.LoginScreen
import com.hd.minitinder.screens.profile.view.ProfileScreen
import com.hd.minitinder.screens.register.view.RegisterScreen
import com.hd.minitinder.screens.payment.view.TinderGoldScreen
import com.hd.minitinder.screens.payment.view.PaymentQRScreen
import com.hd.minitinder.screens.payment.view.PaymentSuccessScreen
import com.hd.minitinder.screens.swipe.view.SwipeScreen
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavigationItem.Swipe.route
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
        composable(NavigationItem.Profile.route){
            ProfileScreen(navController)
        }
        composable(NavigationItem.Main.route){
            MainScreen()
        }
        composable(NavigationItem.Payment.route) {
            TinderGoldScreen(navController)
        }
        composable(
            route = NavigationItem.PaymentQR.route,
            arguments = listOf(navArgument("payment") { type = NavType.StringType }) // Khai báo tham số
        ) { backStackEntry ->
            val payment = backStackEntry.arguments?.getString("payment") ?: "0"
            PaymentQRScreen(navController, payment)
        }

        composable(NavigationItem.PaymentSuccess.route) {
            PaymentSuccessScreen()
        }
        composable(NavigationItem.Swipe.route) {
            SwipeScreen()
        }
    }
}
