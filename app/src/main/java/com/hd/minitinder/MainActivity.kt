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
import android.app.AppOpsManager
import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings


class MainActivity : ComponentActivity() {
    private lateinit var callbackManager: CallbackManager
    private fun hasUsageAccessPermission(): Boolean {
        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun showUsageAccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Yêu cầu quyền truy cập")
            .setMessage("Ứng dụng cần quyền đọc thời gian sử dụng để hoạt động đầy đủ. Hãy cấp quyền ở màn hình tiếp theo.")
            .setPositiveButton("Mở cài đặt") { _, _ ->
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasUsageAccessPermission()) {
            showUsageAccessDialog()
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
