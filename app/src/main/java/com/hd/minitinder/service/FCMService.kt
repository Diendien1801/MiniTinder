package com.hd.minitinder.service

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("send-notification")
    suspend fun sendNotification(@Body request: NotificationRequest)
}

data class NotificationRequest(
    val token: String,
    val title: String,
    val body: String
)


