package com.hd.minitinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.cloudinary.Cloudinary
import com.facebook.CallbackManager
import com.hd.minitinder.navigation.AppNavHost
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.service.CloudinaryManager
import com.hd.minitinder.ui.theme.MiniTinderTheme


class MainActivity : ComponentActivity() {
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cloudinary = CloudinaryManager.cloudinary

        callbackManager = CallbackManager.Factory.create()


        setContent {
            val navController = rememberNavController()

            MiniTinderTheme {
                AppNavHost(navController = navController, callbackManager = callbackManager)
            }

            // Điều hướng sau khi giao diện đã load xong
            intent?.getStringExtra("navigate_to")?.let { destination ->
                if (destination == NavigationItem.Chat.route ) {
                    navController.navigate(NavigationItem.Main.createRoute(NavigationItem.Chat.route))
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
