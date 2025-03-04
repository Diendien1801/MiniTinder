package com.hd.minitinder.screens.detailChat.repositories

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hd.minitinder.screens.detailChat.model.ChatMessageModel

class ChatMessageRepository {
     fun sendMessage(chatId: String, message: ChatMessageModel) {
        val db = Firebase.firestore

        db.collection("chats").document(chatId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener {
                Log.d("Chat", "Tin nhắn đã được gửi!")
            }
            .addOnFailureListener { e ->
                Log.e("Chat", "Gửi tin nhắn thất bại", e)
            }
    }

    // Lắng nghe sự kien real time

    fun listenForMessages(chatId: String, onMessageReceived: (List<ChatMessageModel>) -> Unit) {
        val db = Firebase.firestore
        db.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Chat", "Lỗi khi lắng nghe tin nhắn", error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.map { it.toObject(ChatMessageModel::class.java)!! } ?: emptyList()
                onMessageReceived(messages)
            }
    }

}