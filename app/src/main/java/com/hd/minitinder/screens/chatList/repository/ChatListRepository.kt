package com.hd.minitinder.screens.chatList.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hd.minitinder.data.model.UserModel

class ChatListRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getListChat(userId: String, onResult: (List<String>) -> Unit) {
        val chatList = mutableListOf<String>()

        db.collection("matches")
            .whereEqualTo("idUser1", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val idUser2 = document.getString("idUser2")
                    idUser2?.let { chatList.add(it) }
                }

                // Tiếp tục tìm những document có idUser2 = userId
                db.collection("matches")
                    .whereEqualTo("idUser2", userId)
                    .get()
                    .addOnSuccessListener { documents2 ->
                        for (document in documents2) {
                            val idUser1 = document.getString("idUser1")
                            idUser1?.let { chatList.add(it) }
                        }

                        // Trả kết quả về qua callback
                        onResult(chatList)
                    }
            }
    }
}
