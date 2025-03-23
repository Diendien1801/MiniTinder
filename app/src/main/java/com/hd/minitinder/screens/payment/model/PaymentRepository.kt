package com.hd.minitinder.screens.payment.model

import com.google.firebase.firestore.FirebaseFirestore

class ChatListRepository {

    private val db = FirebaseFirestore.getInstance()

    fun isPremium(userId: String, onResult: (Boolean) -> Unit) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val isPremium = document.getBoolean("isPremium") ?: false
                    val expiredDate = document.getLong("expiredDate") ?: 0L
                    val currentTime = System.currentTimeMillis()

                    onResult(isPremium && expiredDate > currentTime)
                } else {
                    onResult(false) // Người dùng không tồn tại
                }
            }
            .addOnFailureListener {
                onResult(false) // Lỗi Firestore
            }
    }

    fun getPremiumRemain(userId: String, onResult: (Long) -> Unit) { // Millisecond
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val expiredDate = document.getLong("expiredDate") ?: 0L
                    val currentTime = System.currentTimeMillis()

                    val remainingTime = expiredDate - currentTime
                    onResult(if (remainingTime > 0) remainingTime else 0L)
                } else {
                    onResult(0L) // Không tồn tại
                }
            }
            .addOnFailureListener {
                onResult(0L) // Lỗi Firestore
            }
    }

    fun setPremium(userId: String, date: Long, onComplete: (Boolean) -> Unit) {
        val updateData = mapOf(
            "isPremium" to true,
            "expiredDate" to date
        )

        db.collection("users").document(userId)
            .update(updateData)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

}
