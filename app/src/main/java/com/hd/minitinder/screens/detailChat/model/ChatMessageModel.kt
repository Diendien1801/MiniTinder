package com.hd.minitinder.screens.detailChat.model

data class ChatMessageModel(
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
