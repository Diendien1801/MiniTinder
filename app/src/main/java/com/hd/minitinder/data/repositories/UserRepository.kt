package com.hd.minitinder.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.hd.minitinder.data.model.UserModel

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

                for (document in documents) {
                    val idUser2 = document.getString("idUser2") ?: ""
                    Log.d("Firestore", "Processing like document: ${document.id}, idUser2: $idUser2")

                    if (idUser2 == userId) {
                        val idUser1 = document.getString("idUser1") ?: ""
                        Log.d("Firestore", "User $idUser1 liked $userId, fetching details...")

                        getUserById(idUser1) { user ->
                            if (user != null) {
                                likedUsers.add(user)
                                Log.d("Firestore", "Added user: ${user.name} to likedUsers list")
                            }

                            // Nếu đã xử lý xong danh sách, gọi `onResult`
                            if (likedUsers.size == documents.size()) {
                                Log.d("Firestore", "Returning liked users list of size: ${likedUsers.size}")
                                onResult(likedUsers)
                            }
                        }
                    }
                }

                // Nếu không có ai thích userId
                if (likedUsers.isEmpty()) {
                    Log.d("Firestore", "No users liked $userId")
                    onResult(emptyList())
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


}