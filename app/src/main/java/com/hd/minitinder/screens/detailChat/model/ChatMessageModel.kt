package com.hd.minitinder.screens.detailChat.model

data class ChatMessageModel(
    val senderId: String = "",
    val receiverId: String = "",
    var message: String = "",
    var encryptForSender: String = "",
    val timestamp: Long = System.currentTimeMillis() + (7 * 60 * 60 * 1000),
    val type: String = "",
    val read: Boolean = false
)
