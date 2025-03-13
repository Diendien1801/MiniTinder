package com.hd.minitinder.screens.chatList.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.screens.chatList.repository.ChatListRepository
import com.hd.minitinder.screens.login.viewmodel.LoginViewModel


class ChatListViewModel: ViewModel() {

    var chatList = mutableStateOf<List<String>>(emptyList())  // Dùng State để UI cập nhật



    val currentUser = FirebaseAuth.getInstance().currentUser

    private val chatListRepository = ChatListRepository()

    fun getChatList(userId: String = currentUser?.uid ?: "")  {
        Log.d("ChatListViewModel", "getChatList called with userId: ${currentUser?.uid}")
        currentUser?.let {
            chatListRepository.getListChat(it.uid) { list ->
                chatList.value = list
            }
        }
    }
}

