package com.hd.minitinder.screens.swipe.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hd.minitinder.data.model.UserModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class SwipeListRepository {
    private val db = FirebaseFirestore.getInstance();

    fun getFormattedTimestamp(): String {
        val timestamp = Timestamp.now()

        val date = timestamp.toDate()

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(date)
    }

    fun getUserList(userId: String, onResult: (List<String>) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val userIds = result.documents
                    .map { it.id }
                    .filter { it != userId }
                onResult(userIds)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getMissList(userId: String, onResult: (List<String>) -> Unit) {
        db.collection("misses")
            .whereEqualTo("idUser1", userId)
            .get()
            .addOnSuccessListener { result ->
                val missedIds = result.documents
                    .mapNotNull { it.getString("idUser2") }

                onResult(missedIds)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getMatchList(userId: String, onResult: (List<String>) -> Unit) {
        db.collection("matches")
            .whereIn("idUser1", listOf(userId))
            .get()
            .addOnSuccessListener { result1 ->
                db.collection("matches")
                    .whereIn("idUser2", listOf(userId))
                    .get()
                    .addOnSuccessListener { result2 ->
                        val matchedIds = (result1.documents + result2.documents)
                            .mapNotNull {
                                val id1 = it.getString("idUser1")
                                val id2 = it.getString("idUser2")
                                if (id1 == userId) id2 else id1
                            }
                        onResult(matchedIds.distinct()) // Trả về danh sách không trùng lặp
                    }
                    .addOnFailureListener { onResult(emptyList()) }
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun miss(userId1: String, userId2: String, onComplete: (Boolean) -> Unit) {
        val missData = hashMapOf(
            "idUser1" to userId1,
            "idUser2" to userId2,
            "timestamp" to getFormattedTimestamp(),
        )

        db.collection("misses")
            .add(missData)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun like(userId: String, userId2: String, onComplete: (Boolean) -> Unit) {
        val likesRef = db.collection("likes")
        val matchesRef = db.collection("matches")

        likesRef.whereEqualTo("idUser1", userId2)
            .whereEqualTo("idUser2", userId)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val docId = result.documents[0].id
                    likesRef.document(docId).delete()
                        .addOnSuccessListener {
                            val matchData = hashMapOf(
                                "idUser1" to userId,
                                "idUser2" to userId2,
                                "timestamp" to getFormattedTimestamp(),
                            )
                            matchesRef.add(matchData)
                                .addOnSuccessListener { onComplete(true) } // Thành công
                                .addOnFailureListener { onComplete(false) }
                        }
                        .addOnFailureListener { onComplete(false) }
                } else {
                    val likeData = hashMapOf(
                        "idUser1" to userId,
                        "idUser2" to userId2,
                        "timestamp" to getFormattedTimestamp(),
                    )
                    likesRef.add(likeData)
                        .addOnSuccessListener { onComplete(true) }
                        .addOnFailureListener { onComplete(false) }
                }
            }
            .addOnFailureListener { onComplete(false) }
    }

    fun getGoBackList(userId: String, onResult: (List<String>) -> Unit) {
        db.collection("goback")
            .whereEqualTo("idUser1", userId)
            .get()
            .addOnSuccessListener { result ->
                val goBackIds = result.documents
                    .sortedByDescending { it.id }
                    .mapNotNull { it.getString("idUser2") }
                onResult(goBackIds)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun deleteMiss(userId1: String, userId2: String, onComplete: (Boolean) -> Unit) {
        db.collection("misses")
            .whereEqualTo("idUser1", userId1)
            .whereEqualTo("idUser2", userId2)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    for (document in result.documents) {
                        db.collection("misses").document(document.id)
                            .delete()
                            .addOnSuccessListener { onComplete(true) }
                            .addOnFailureListener { onComplete(false) }
                    }
                } else {
                    onComplete(false) // Không tìm thấy document để xóa
                }
            }
            .addOnFailureListener { onComplete(false) }
    }

}