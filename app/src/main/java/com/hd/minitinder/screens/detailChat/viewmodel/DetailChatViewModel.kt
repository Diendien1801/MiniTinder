import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hd.minitinder.screens.detailChat.model.ChatMessageModel
import com.hd.minitinder.screens.detailChat.repositories.ChatMessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    fun initChat(chatId: String, userId: String, receiverId: String) {
        _chatId.value = chatId
        _userId.value = userId
        _receiverId.value = receiverId
        Log.d("Chat2", "ChatId: ${_chatId.value}, Sender: ${_userId.value}, Receiver: ${_receiverId.value}")

        listenForMessages()
    }

    fun sendMessage(message: String) {
        val chat = _chatId.value ?: return
        val sender = _userId.value ?: return
        val receiver = _receiverId.value ?: return

        if (message.isBlank()) return
        Log.d("Chat", "ChatId: $chat, Message: $message, Sender: $sender, Receiver: $receiver")

        val chatMessage = ChatMessageModel(senderId = sender, receiverId = receiver, message = message)
        repository.sendMessage(chat, chatMessage)
    }

    private fun listenForMessages() {
        val chat = _chatId.value ?: return
        repository.listenForMessages(chat) { newMessages ->
            viewModelScope.launch {
                _messages.emit(newMessages)
            }
        }
    }

    fun getUserId(): String {
        return _userId.value ?: ""

    }
}
