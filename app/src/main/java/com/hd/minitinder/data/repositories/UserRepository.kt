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

}