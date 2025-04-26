import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface PaymentApiService {
    @POST("create-checkout-session")
    suspend fun createCheckoutSession(@Body request: CheckoutRequest): CheckoutResponse
}

data class CheckoutRequest(
    val userId: String,
    val price: Int
)

data class CheckoutResponse(
    val url: String
)

object RetrofitClient {
    val paymentApi: PaymentApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:4242/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymentApiService::class.java)
    }
}