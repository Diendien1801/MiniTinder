import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hd.minitinder.screens.detailChat.model.ChatMessageModel
import com.hd.minitinder.screens.detailChat.repositories.ChatMessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailChatViewModel() : ViewModel() {
    init {
        Log.d("ChatViewModel", "ViewModel Created")
    }

    private val _messages = MutableStateFlow<List<ChatMessageModel>>(emptyList())
    val messages = _messages.asStateFlow()

    private val repository: ChatMessageRepository = ChatMessageRepository()

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

    fun sendMessage(message: String) {
        val chat = _chatId.value ?: return
        val sender = _userId.value ?: return
        val receiver = _receiverId.value ?: return

        if (message.isBlank()) return
        Log.d("Chat", "ChatId: $chat, Message: $message, Sender: $sender, Receiver: $receiver")

        val chatMessage = ChatMessageModel(
            senderId = sender,
            receiverId = receiver,
            message = message,
            type = "text"
        )
        repository.sendMessage(chat, chatMessage, sender,receiver)
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
                            type = "image" // Đánh dấu tin nhắn là ảnh
                        )
                    }
                    chatId.value?.let {
                        if (message != null) {
                            repository.sendMessage(
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


}
