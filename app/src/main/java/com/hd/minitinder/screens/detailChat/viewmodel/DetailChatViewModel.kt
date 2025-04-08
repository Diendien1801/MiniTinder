import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.hd.minitinder.data.repositories.UserRepository
import com.hd.minitinder.screens.detailChat.model.ChatMessageModel
import com.hd.minitinder.screens.detailChat.repositories.ChatMessageRepository
import com.hd.minitinder.service.ApiService
import com.hd.minitinder.service.NotificationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailChatViewModel() : ViewModel() {
    init {
        Log.d("ChatViewModel", "ViewModel Created")
    }

    private val _messages = MutableStateFlow<List<ChatMessageModel>>(emptyList())
    val messages = _messages.asStateFlow()

    private val repository: ChatMessageRepository = ChatMessageRepository()
    private val userRepository: UserRepository = UserRepository()
    private val _chatId = MutableStateFlow<String?>("")
    val chatId = _chatId.asStateFlow()

    private val _userId = MutableStateFlow<String?>(Firebase.auth.currentUser?.uid)
    val userId = _userId.asStateFlow()

    private val _receiverId = MutableStateFlow<String?>("")
    val receiverId = _receiverId.asStateFlow()



    fun initChat(chatId: String, userId: String, receiverId: String, context: Context) {
        _chatId.value = chatId
        _userId.value = userId
        _receiverId.value = receiverId
        Log.d("Chat2", "ChatId: ${_chatId.value}, Sender: ${_userId.value}, Receiver: ${_receiverId.value}")

        listenForMessages(context)
    }

    fun sendMessage(context: Context, message: String) {
        val chat = _chatId.value ?: return
        val sender = _userId.value ?: return
        val receiver = _receiverId.value ?: return

        if (message.isBlank()) return
        Log.d("Chat", "ChatId: $chat, Message: $message, Sender: $sender, Receiver: $receiver")

        val chatMessage = ChatMessageModel(
            senderId = sender,
            receiverId = receiver,
            message = message,
            type = "text",
            read = false
        )

        // Gửi tin nhắn đến Firestore
        repository.sendMessage(context, chat, chatMessage, sender, receiver)

        // Gửi thông báo FCM khi có tin nhắn mới
        viewModelScope.launch {
            try {
                val token = withContext(Dispatchers.IO) {
                    userRepository.getReceiverToken(receiver)
                }

                if (!token.isNullOrBlank()) {
                    sendPushNotification(token, "Tin nhắn mới", message)
                } else {
                    Log.e("FCM", "Không tìm thấy token của người nhận")
                }
            } catch (e: Exception) {
                Log.e("FCM", "Lỗi khi gửi thông báo: ${e.message}")
            }
        }
    }



    // Gửi thông báo
    suspend fun sendPushNotification(token: String, title: String, body: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.120.3.201:3000") //192.168.1.10
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val request = NotificationRequest(token, title, body)

        val response = apiService.sendNotification(request)
        Log.d("FCM", "Gửi thông báo thành công: $response")
    }
    private fun listenForMessages(context: Context) {
        val chat = _chatId.value ?: return
        // Lấy private key từ SharedPreferences
        val privateKey =
            Firebase.auth.currentUser?.uid?.let {
                SharedPreferencesManager.getPrivateKey(context,
                    it
                )
            }

        if (privateKey == null) {
            Log.e("Chat", "Private key is null!")
            return
        }

        repository.listenForMessages(chat,  userId.value.toString(),privateKey,) { newMessages ->
            viewModelScope.launch {
                _messages.emit(newMessages)
                markAllMessagesAsRead(context, userId.value.toString()){

                }
            }
        }
    }


    fun getUserId(): String {
        return _userId.value ?: ""
    }

    fun sendImageMessage(context: Context, imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = repository.uploadImageToCloudinary(context, imageUri) // Chờ kết quả upload
                if (imageUrl != null) {
                    val message = receiverId.value?.let {
                        ChatMessageModel(
                            senderId = getUserId(),
                            receiverId = it,
                            message = imageUrl, // Lưu URL ảnh
                            type = "image", // Đánh dấu tin nhắn là ảnh
                            read = false
                        )
                    }
                    chatId.value?.let {
                        if (message != null) {
                            repository.sendMessage(
                                context,
                                it,
                                message,
                                senderId = userId.value.toString(),
                                receiverId = receiverId.value.toString(),
                            )
                        }
                    }
                } else {
                    Log.e("Chat", "Upload ảnh thất bại")
                }
            } catch (e: Exception) {
                Log.e("Chat", "Lỗi gửi ảnh: ${e.message}")
            }
        }
    }

    // update read all message
    fun markAllMessagesAsRead(context: Context, currentUserId: String, onComplete: (Boolean) -> Unit) {
        val chatId = _chatId.value ?: return
        repository.markAllMessagesAsRead(chatId, currentUserId) { success ->
            onComplete(success)
        }
    }


}
