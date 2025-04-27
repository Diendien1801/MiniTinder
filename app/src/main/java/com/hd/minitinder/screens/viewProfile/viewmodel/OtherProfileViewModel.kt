package com.hd.minitinder.screens.viewProfile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.data.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class OtherProfileViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val userRepository = UserRepository()

    private val _userProfileState = MutableStateFlow<UserModel?>(null)
    val userProfileState: StateFlow<UserModel?> = _userProfileState

//    fun getUserById(userId: String) {
//        viewModelScope.launch {
//            try {
//                val user = userRepository.getUserById(userId) // Gọi hàm từ UserRepository
//                _userProfileState.value = user // Cập nhật thông tin người dùng vào StateFlow
//            } catch (e: Exception) {
//                Log.e("OtherProfileViewModel", "Error fetching user: ${e.message}")
//            }
//        }
//    }

    suspend fun loadUserProfile(userId: String) {
        try {
            // Gọi Firestore để lấy dữ liệu người dùng
            val snapshot = firestore.collection("users").document(userId).get().await()

            // Kiểm tra xem tài liệu người dùng có tồn tại không
            if (snapshot.exists()) {
                val user = UserModel.fromJson(snapshot)
                Log.d("aloalo"," ${user.name}")
                _userProfileState.value = user // Cập nhật StateFlow với dữ liệu người dùng

            } else {
                _userProfileState.value = null // Nếu không tìm thấy người dùng, đặt giá trị null
            }
        } catch (e: Exception) {
            Log.e("OtherProfileViewModel", "Error fetching user: ${e.message}")
            _userProfileState.value = null // Cập nhật giá trị null nếu có lỗi
        }
    }

    fun blockUser(targetUserId: String, onComplete: (Boolean) -> Unit) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        firestore.collection("blocks")
            .document(currentUserId)
            .collection("blocked")
            .document(targetUserId)
            .set(mapOf("timestamp" to FieldValue.serverTimestamp()))
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun unmatchUser(targetUserId: String, onComplete: (Boolean) -> Unit) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        firestore.collection("matches")
            .whereIn("idUser1", listOf(currentUserId, targetUserId))
            .get()
            .addOnSuccessListener { querySnapshot ->
                val matchDoc = querySnapshot.documents.firstOrNull { doc ->
                    val id1 = doc.getString("idUser1")
                    val id2 = doc.getString("idUser2")
                    (id1 == currentUserId && id2 == targetUserId) ||
                            (id1 == targetUserId && id2 == currentUserId)
                }

                if (matchDoc != null) {
                    firestore.collection("matches")
                        .document(matchDoc.id)
                        .delete()
                        .addOnSuccessListener { onComplete(true) }
                        .addOnFailureListener { onComplete(false) }
                } else { onComplete(true) }
            }
            .addOnFailureListener { onComplete(false) }
    }

    suspend fun isMatch(userId1: String, userId2: String): Boolean {
        val db = FirebaseFirestore.getInstance()
        val matchesRef = db.collection("matches")

        // Kiểm tra xem tài liệu "match" có tồn tại giữa user1 và user2 không
        val matchDoc = matchesRef
            .whereEqualTo("idUser1", userId1)
            .whereEqualTo("idUser2", userId2)
            .get()
            .await()

        // Nếu tài liệu tồn tại, hai người dùng đã "match"
        return !matchDoc.isEmpty
    }

    fun getCurrentUserId(): String {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        return currentUserId.orEmpty() // Trả về empty string nếu currentUserId là null
    }

}
