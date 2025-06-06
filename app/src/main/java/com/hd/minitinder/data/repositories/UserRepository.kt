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
                    isPremium = false,

                )

                userRef.set(newUser.toJson()) // Lưu user vào Firestore
                    .addOnSuccessListener { Log.d("Firestore", "User saved successfully!") }
                    .addOnFailureListener { e -> Log.e("Firestore", "Error saving user: ${e.message}") }
            } else {
                Log.d("Firestore", "User already exists!")
            }
        }
    }
    suspend fun getUserById(userId: String): UserModel? {
        val db = FirebaseFirestore.getInstance()
        val document = try {
            // Lấy tài liệu người dùng bất đồng bộ và đợi kết quả
            db.collection("users").document(userId).get().await()
        } catch (e: Exception) {
            // Nếu có lỗi, trả về null
            return null
        }

        return if (document.exists()) {
            UserModel.fromJson(document) // Trả về đối tượng UserModel nếu tài liệu tồn tại
        } else {
            null // Trả về null nếu không tìm thấy tài liệu
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


    suspend fun isPremium(userId: String): Boolean {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")
        val document = usersRef.document(userId).get().await()

        return if (document.exists()) {
            document.getBoolean("isPremium") ?: false
        } else {
            false
        }
    }

    fun updateUserToDatabase(user: UserModel) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(user.id)

        val userMap = user.toJson() // Chỉ lấy các trường có giá trị hợp lệ
        Log.d("Firestore", "Updating user: ${userMap}")
        if (userMap.isNotEmpty()) {
            userRef.update(userMap)
                .addOnSuccessListener {
                    Log.d("Firestore", "User updated successfully!")
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error updating user: ${exception.message}")
                }
        } else {
            Log.d("Firestore", "No fields to update.")
        }
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
    suspend fun getPrivateKey(userId: String): String? {
        return try {
            Log.d("KeyManagement", "Bắt đầu lấy private key cho userId: $userId")

            val document = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .await()  // Đợi dữ liệu trả về (suspend function)

            Log.d("KeyManagement", "Document lấy được: ${document.data}")

            val privateKey = document.getString("privateKey")

            if (privateKey != null) {
                Log.d("KeyManagement", "Private key lấy được: $privateKey")
            } else {
                Log.w("KeyManagement", "Không tìm thấy private key cho userId: $userId")
            }

            privateKey
        } catch (e: Exception) {
            Log.e("KeyManagement", "Lỗi khi lấy private key từ Firestore", e)
            null
        }
    }


    fun updateUserImage(
        userId: String,
        imageUrls: List<String>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        Log.d("FirestoreDebug", "Bắt đầu cập nhật ảnh cho userId: $userId")

        if (userId.isBlank()) {
            val error = Exception("Lỗi: userId rỗng!")
            Log.e("FirestoreDebug", error.message ?: "Lỗi không xác định")
            onFailure(error)
            return
        }

        if (imageUrls.isEmpty()) {
            val error = Exception("Lỗi: Danh sách ảnh rỗng!")
            Log.e("FirestoreDebug", error.message ?: "Lỗi không xác định")
            onFailure(error)
            return
        }

        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")

        Log.d("FirestoreDebug", "Chuẩn bị cập nhật imageUrls: $imageUrls")

        usersRef.document(userId).update("imageUrls", imageUrls)
            .addOnSuccessListener {
                Log.d("FirestoreDebug", "Cập nhật ảnh thành công cho userId: $userId")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreDebug", "Lỗi khi cập nhật ảnh: ${exception.message}")
                onFailure(exception)
            }
    }
}