package com.hd.minitinder.screens.chatList.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hd.minitinder.data.model.UserModel

class ChatListRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getListChat(userId: String, onResult: (List<Pair<String, String>>) -> Unit) {
        val chatList = mutableListOf<Pair<String, String>>()

        db.collection("matches")
            .whereEqualTo("idUser1", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val idUser2 = document.getString("idUser2")
                    val docId = document.id
                    idUser2?.let { chatList.add(it to docId) }
                }

                // Tiếp tục tìm những document có idUser2 = userId
                db.collection("matches")
                    .whereEqualTo("idUser2", userId)
                    .get()
                    .addOnSuccessListener { documents2 ->
                        for (document in documents2) {
                            val idUser1 = document.getString("idUser1")
                            val docId = document.id
                            idUser1?.let { chatList.add(it to docId) }
                        }

                        // Trả kết quả về qua callback
                        onResult(chatList)
                    }
            }
    }

    fun getListUser(chatListId: List<String>, onResult: (List<UserModel>) -> Unit) {
        if (chatListId.isEmpty()) {
            onResult(emptyList()) // Nếu danh sách rỗng, trả về ngay
            return
        }

        val usersMap = mutableMapOf<String, UserModel>() // Lưu theo ID để giữ thứ tự
        val dbRef = db.collection("users")
        var remaining = chatListId.size

        for (userId in chatListId) {
            dbRef.document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = UserModel(
                            id = document.getString("id") ?: "",
                            name = document.getString("name") ?: "Unknown",
                            imageUrls = document.get("imageUrls") as? List<String> ?: emptyList()
                        )
                        usersMap[userId] = user // Lưu vào map với key là userId
                    }
                }
                .addOnCompleteListener {
                    remaining--
                    if (remaining == 0) {
                        // Sắp xếp danh sách theo thứ tự trong chatListId
                        val orderedUsers = chatListId.mapNotNull { usersMap[it] }
                        onResult(orderedUsers)
                    }
                }
        }
    }




    fun checkUnreadMessage(chatId: String, userId: String, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        Log.d("checkUnreadMessage", "Checking unread message for chatId: $chatId, userId: $userId")

        db.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                Log.d("checkUnreadMessage", "Query successful, documents found: ${snapshot.documents.size}")

                val latestMessage = snapshot.documents.firstOrNull()
                val senderId = latestMessage?.getString("senderId")
                val isRead = latestMessage?.getBoolean("read") ?: true

                Log.d("checkUnreadMessage", "Latest message senderId: $senderId, isRead: $isRead")

                // Kiểm tra nếu tin nhắn chưa được đọc và không phải do chính người dùng gửi
                val hasUnreadMessage = senderId != userId && !isRead
                Log.d("checkUnreadMessage", "Unread message status: $hasUnreadMessage")

                callback(hasUnreadMessage)
            }
            .addOnFailureListener { exception ->
                Log.e("checkUnreadMessage", "Error fetching messages", exception)
                callback(false)
            }
    }



}
