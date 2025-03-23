package com.hd.minitinder.screens.chatList.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.screens.chatList.repository.ChatListRepository
import com.hd.minitinder.screens.login.viewmodel.LoginViewModel


class ChatListViewModel : ViewModel() {

    var chatList = mutableStateOf<List<String>>(emptyList())
    var chatIdList = mutableStateOf<List<String>>(emptyList())
    var userList = mutableStateOf<List<UserModel>>(emptyList())

    var isLoading = mutableStateOf(false)
    val currentUser = FirebaseAuth.getInstance().currentUser

    private val chatListRepository = ChatListRepository()

    fun getChatList(userId: String = currentUser?.uid ?: "") {
        Log.d("ChatListViewModel", "getChatList called with userId: $userId")

        if (userId.isEmpty()) return

        isLoading.value = true

        chatListRepository.getListChat(userId) { list ->
            chatList.value = list.map { it.first }
            chatIdList.value = list.map { it.second }

            getUserList()
        }
    }

    fun getUserList() {
        chatListRepository.getListUser(chatList.value) { list ->
            userList.value = list
            isLoading.value = false // Kết thúc loading
        }
    }
}


