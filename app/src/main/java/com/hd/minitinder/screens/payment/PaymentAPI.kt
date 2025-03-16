package com.hd.minitinder.screens.payment

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PaymentApiService {
    @GET("payment/status")
    suspend fun checkPaymentStatus(@Query("orderId") orderId: String): PaymentStatusResponse
}

data class PaymentStatusResponse(val status: String)

object PaymentApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/") // Thay bằng URL thật của backend
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: PaymentApiService = retrofit.create(PaymentApiService::class.java)
}