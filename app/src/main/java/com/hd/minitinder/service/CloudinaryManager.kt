package com.hd.minitinder.service

import android.net.Uri
import com.cloudinary.Cloudinary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object CloudinaryManager {
    val cloudinary: Cloudinary by lazy {
        Cloudinary(
            mapOf(
                "cloud_name" to "dwbys1qae",
                "api_key" to "398141431225165",
                "api_secret" to "G_1MAflKezR_cCk1ovZeBQDWueU"
            )
        )
    }


}
