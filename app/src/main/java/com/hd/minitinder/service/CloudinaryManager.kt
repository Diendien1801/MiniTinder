package com.hd.minitinder.service

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

object CloudinaryManager {
    private const val TAG = "CloudinaryUpload"
    val cloudinary: Cloudinary by lazy {
        Cloudinary(
            mapOf(
                "cloud_name" to "dwbys1qae",
                "api_key" to "398141431225165",
                "api_secret" to "G_1MAflKezR_cCk1ovZeBQDWueU"
            )
        )
    }
    suspend fun uploadImageToCloudinary(context: Context, imageUri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                inputStream?.use { stream ->  // Đảm bảo đóng InputStream sau khi dùng
                    val result = CloudinaryManager.cloudinary.uploader().upload(
                        stream,
                        ObjectUtils.emptyMap()
                    )
                    result["secure_url"] as String
                }
            } catch (e: Exception) {
                Log.e("Upload", "Lỗi upload: ${e.message}")
                null
            }
        }
    }


}
