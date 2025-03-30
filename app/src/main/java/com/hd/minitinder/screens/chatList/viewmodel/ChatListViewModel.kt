package com.hd.minitinder.screens.chatList.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.screens.chatList.repository.ChatListRepository
import kotlinx.coroutines.launch

class ChatListViewModel : ViewModel() {

    // partnerID
    var chatList = mutableStateOf<List<String>>(emptyList())
    // Chat ID
    var chatIdList = mutableStateOf<List<String>>(emptyList())
    var userList = mutableStateOf<List<UserModel>>(emptyList())

    // Trạng thái tin nhắn chưa đọc (Map: chatId -> có tin nhắn chưa đọc hay không)
    var unreadChats = mutableStateOf<Map<String, Boolean>>(emptyMap()) // Lưu trạng thái chưa đọc

    var isLoading = mutableStateOf(false)
    val currentUser = FirebaseAuth.getInstance().currentUser


    var mappedUserList = mutableStateOf<List<Pair<UserModel, Boolean>>>(emptyList())
    private val chatListRepository = ChatListRepository()

    fun getChatList(userId: String = currentUser?.uid ?: "") {
        Log.d("ChatListViewModel", "getChatList called with userId: $userId")

        if (userId.isEmpty()) return

        isLoading.value = true

        chatListRepository.getListChat(userId) { list ->
            chatList.value = list.map { it.first }
            chatIdList.value = list.map { it.second }

            getUserList()
            checkUnreadMessages()
        }
    }

    fun getUserList() {
        chatListRepository.getListUser(chatList.value) { list ->
            userList.value = list
            isLoading.value = false // Kết thúc loading
        }
    }

    fun checkUnreadMessages() {
        val updatedUnreadChats = mutableMapOf<String, Boolean>()

        viewModelScope.launch {
            chatIdList.value.forEach { chatId ->
                chatListRepository.checkUnreadMessage(chatId, currentUser?.uid ?: "") { isUnread ->
                    updatedUnreadChats[chatId] = isUnread

                    // Khi đã kiểm tra hết tất cả chatId, cập nhật unreadChats một lần duy nhất
                    if (updatedUnreadChats.size == chatIdList.value.size) {
                        unreadChats.value = updatedUnreadChats
                        updateMappedUserList()
                    }
                }
            }
        }
    }
    fun updateMappedUserList() {
        mappedUserList.value = userList.value.zip(chatIdList.value) { user, chatId ->
            user to (unreadChats.value[chatId] ?: false)
        }
    }
}
