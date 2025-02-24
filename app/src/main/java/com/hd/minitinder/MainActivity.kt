package com.hd.minitinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import com.hd.minitinder.navigation.AppNavHost
import com.hd.minitinder.ui.theme.MiniTinderTheme

class MainActivity : ComponentActivity() {
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callbackManager = CallbackManager.Factory.create()

        setContent {
            val navController = rememberNavController()
            MiniTinderTheme {
                AppNavHost(navController = navController, callbackManager = callbackManager)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
