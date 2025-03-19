package com.hd.minitinder.screens.chatList.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.screens.chatList.repository.ChatListRepository


class ChatListViewModel: ViewModel() {

    var chatList = mutableStateOf<List<String>>(emptyList())  // Dùng State để UI cập nhật
    var userList = mutableStateOf<List<UserModel>>(emptyList())


    val currentUser = FirebaseAuth.getInstance().currentUser

    private val chatListRepository = ChatListRepository()

    fun getChatList(userId: String = currentUser?.uid ?: "")  {
        Log.d("ChatListViewModel", "getChatList called with userId: $userId")
        currentUser?.let {
            chatListRepository.getListChat(it.uid) { list ->
                chatList.value = list
                getUserList()
            }
        }
    }
    fun getUserList(){
        chatListRepository.getListUser(chatList.value) {
            list -> userList.value = list
        }
    }
}

