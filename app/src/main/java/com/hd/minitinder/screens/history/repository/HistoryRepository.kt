package com.hd.minitinder.screens.history.repository

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hd.minitinder.data.model.UserModel
import kotlinx.coroutines.tasks.await

class HistoryRepository {

    // get history from server
    suspend fun getLikeActivity(userId: String): List<UserModel> {
        val db = Firebase.firestore
        val likedUserIds = mutableListOf<String>()
        val userList = mutableListOf<UserModel>()

        try {
            Log.d("FirebaseDebug", "Bắt đầu lấy danh sách like cho userId: $userId")

            // Truy vấn danh sách ID từ collection "likes"
            val likeSnapshot = db.collection("likes")
                .whereEqualTo("idUser1", userId)
                .get()
                .await() // Đợi kết quả

            Log.d("FirebaseDebug", "Lấy dữ liệu từ collection 'likes', số lượng: ${likeSnapshot.documents.size}")

            for (doc in likeSnapshot.documents) {
                val likedId = doc.getString("idUser2") // Lấy ID của người được thích
                likedId?.let {
                    likedUserIds.add(it)
                    Log.d("FirebaseDebug", "Lấy được idUser2: $likedId")
                }
            }

            // Nếu có ID hợp lệ thì lấy thông tin từ "users"
            if (likedUserIds.isNotEmpty()) {
                Log.d("FirebaseDebug", "Bắt đầu lấy thông tin người dùng với ${likedUserIds.size} ID")

                val userSnapshot = db.collection("users")
                    .whereIn("id", likedUserIds)
                    .get()
                    .await()

                Log.d("FirebaseDebug", "Lấy dữ liệu từ collection 'users', số lượng: ${userSnapshot.documents.size}")

                for (doc in userSnapshot.documents) {
                    val user = doc.toObject(UserModel::class.java)
                    user?.let {
                        userList.add(it)
                        Log.d("FirebaseDebug", "Thêm user: ${user.id} - ${user.name}")
                    }
                }
            } else {
                Log.d("FirebaseDebug", "Không có userId nào trong danh sách like")
            }
        } catch (e: Exception) {
            Log.e("FirebaseError", "Lỗi khi lấy danh sách thích: ${e.message}", e)
        }

        return userList
    }

    suspend fun getMatchedUsers(userId: String): List<UserModel> {
        val db = Firebase.firestore
        val matchedUserIds = mutableSetOf<String>() // Dùng Set để tránh trùng lặp
        val userList = mutableListOf<UserModel>()

        try {
            Log.d("FirebaseDebug", "Bắt đầu lấy danh sách match cho userId: $userId")

            // Truy vấn danh sách matches có userId là idUser1 hoặc idUser2
            val matchSnapshot = db.collection("matches")
                .whereIn("idUser1", listOf(userId))
                .get()
                .await()

            val matchSnapshot2 = db.collection("matches")
                .whereIn("idUser2", listOf(userId))
                .get()
                .await()

            Log.d("FirebaseDebug", "Lấy dữ liệu từ collection 'matches', số lượng: ${matchSnapshot.documents.size + matchSnapshot2.documents.size}")

            // Duyệt qua danh sách match
            for (doc in matchSnapshot.documents) {
                val matchedId = doc.getString("idUser2") // Người được match với userId
                matchedId?.let {
                    matchedUserIds.add(it)
                    Log.d("FirebaseDebug", "Lấy được idUser2 (matched): $matchedId")
                }
            }
            for (doc in matchSnapshot2.documents) {
                val matchedId = doc.getString("idUser1") // Người được match với userId
                matchedId?.let {
                    matchedUserIds.add(it)
                    Log.d("FirebaseDebug", "Lấy được idUser1 (matched): $matchedId")
                }
            }

            // Nếu có ID hợp lệ thì lấy thông tin từ "users"
            if (matchedUserIds.isNotEmpty()) {
                Log.d("FirebaseDebug", "Bắt đầu lấy thông tin người dùng với ${matchedUserIds.size} ID")

                val userSnapshot = db.collection("users")
                    .whereIn("id", matchedUserIds.toList()) // Chuyển set thành list
                    .get()
                    .await()

                Log.d("FirebaseDebug", "Lấy dữ liệu từ collection 'users', số lượng: ${userSnapshot.documents.size}")

                for (doc in userSnapshot.documents) {
                    val user = doc.toObject(UserModel::class.java)
                    user?.let {
                        userList.add(it)
                        Log.d("FirebaseDebug", "Thêm user: ${user.id} - ${user.name}")
                    }
                }
            } else {
                Log.d("FirebaseDebug", "Không có userId nào trong danh sách match")
            }
        } catch (e: Exception) {
            Log.e("FirebaseError", "Lỗi khi lấy danh sách match: ${e.message}", e)
        }

        return userList
    }

}
