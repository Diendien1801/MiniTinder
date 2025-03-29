package com.hd.minitinder.data.repositories

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hd.minitinder.data.model.UserModel
import kotlinx.coroutines.tasks.await

class UserRepository {
    fun saveUserToFirestore(user: UserModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(user.id)

        userRef.set(user.toJson())
            .addOnSuccessListener {
                onSuccess() // Gọi khi lưu thành công
            }
            .addOnFailureListener { exception ->
                onFailure(exception) // Gọi khi có lỗi
            }
    }


    //// FOR FACEBOOK
    // ✅ Hàm kiểm tra & lưu user vào Firestore
    fun checkAndSaveUser(firebaseUser: FirebaseUser) {
        val userRef = FirebaseFirestore.getInstance().collection("users").document(firebaseUser.uid)

        userRef.get().addOnSuccessListener { document ->
            if (!document.exists()) { // Nếu user chưa có trong Firestore
                val newUser = UserModel(
                    id = firebaseUser.uid,
                    imageUrls = listOf(firebaseUser.photoUrl?.toString() ?: ""),
                    name = firebaseUser.displayName ?: "Unknown",
                    gender = "",
                    dob = "",
                    hometown = "",
                    job = "",
                    interests = listOf(),
                    height = 0.0,
                    weight = 0.0,
                    phoneNumber = firebaseUser.phoneNumber ?: "",
                    bio = "",
                    isPremium = false
                )

                userRef.set(newUser.toJson()) // Lưu user vào Firestore
                    .addOnSuccessListener { Log.d("Firestore", "User saved successfully!") }
                    .addOnFailureListener { e -> Log.e("Firestore", "Error saving user: ${e.message}") }
            } else {
                Log.d("Firestore", "User already exists!")
            }
        }
    }
    fun getUserById(userId: String, onResult: (UserModel?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")

        Log.d("Firestore", "Fetching user with ID: $userId")

        usersRef.document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = UserModel.fromJson(document)
                    Log.d("Firestore", "User found: $user")
                    onResult(user)
                } else {
                    Log.d("Firestore", "User not found: $userId")
                    onResult(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching user: $userId", e)
                onResult(null)
            }
    }



    // Danh sách người thích userID
    fun getLikedUsers(userId: String, onResult: (List<UserModel>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val likesRef = db.collection("likes")

        Log.d("Firestore", "Fetching liked users for user ID: $userId")

        likesRef.get()
            .addOnSuccessListener { documents ->
                val likedUsers = mutableListOf<UserModel>()
                val tasks = mutableListOf<Task<DocumentSnapshot>>() // Danh sách các task để chờ tất cả hoàn tất

                for (document in documents) {
                    val idUser2 = document.getString("idUser2") ?: ""
                    if (idUser2 == userId) {
                        val idUser1 = document.getString("idUser1") ?: ""
                        Log.d("Firestore", "User $idUser1 liked $userId, fetching details...")

                        val task = db.collection("users").document(idUser1).get()
                            .addOnSuccessListener { userDoc ->
                                if (userDoc.exists()) {
                                    val user = UserModel.fromJson(userDoc)
                                    likedUsers.add(user)
                                    Log.d("Firestore", "Added user: ${user.name} to likedUsers list")
                                }
                            }
                        tasks.add(task)
                    }
                }

                Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Returning liked users list of size: ${likedUsers.size}")
                        onResult(likedUsers)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching liked users for $userId", e)
                onResult(emptyList())
            }
    }


    fun isPremium(userId: String): Boolean {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")
        var isPremium = false
        usersRef.document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    isPremium = document.getBoolean("isPremium") ?: false
                }
                }
        return isPremium
    }

    suspend fun getReceiverToken(receiverId: String): String? {
        return try {
            val document = FirebaseFirestore.getInstance()
                .collection("users")
                .document(receiverId)
                .get()
                .await()  // Đợi dữ liệu trả về (suspend function)

            document.getString("fcmToken")
        } catch (e: Exception) {
            Log.e("FCM", "Lỗi khi lấy token từ Firestore", e)
            null
        }
    }


}