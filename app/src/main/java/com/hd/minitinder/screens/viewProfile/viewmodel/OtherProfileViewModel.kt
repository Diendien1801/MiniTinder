package com.hd.minitinder.screens.viewProfile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.hd.minitinder.data.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OtherProfileViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _userProfileState = MutableStateFlow<UserModel?>(null)
    val userProfileState: StateFlow<UserModel?> = _userProfileState

    fun loadUserProfile(userId: String) {
        // Sử dụng Firestore để load dữ liệu của người dùng có userId tương ứng
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = UserModel.fromJson(snapshot)
                _userProfileState.value = user
            }
            .addOnFailureListener {
                // Có thể log lỗi hoặc cập nhật UI thông báo lỗi
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


}
