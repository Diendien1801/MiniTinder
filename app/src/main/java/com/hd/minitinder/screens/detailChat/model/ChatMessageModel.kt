package com.hd.minitinder.screens.detailChat.model

data class ChatMessageModel(
    val senderId: String = "",
    val receiverId: String = "",
    var message: String = "",
    var encryptForSender: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "",
    val read: Boolean = false
)
