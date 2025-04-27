package com.hd.minitinder.screens.swipe.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hd.minitinder.screens.swipe.repository.SwipeListRepository
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UserProfile(
    val id: String,
    val name: String,
    val age: Int,
    val imageUrls: List<String>,
    val tags: List<String>,
    val address: String,
    val occupation: String,
    val bio: String
)

class SwipeViewModel : ViewModel() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    private val _userState = MutableStateFlow(UserModel())
    val userState: StateFlow<UserModel> = _userState

    private val firestore = FirebaseFirestore.getInstance()
    init {
        currentUser?.uid?.let { userId ->
            loadUserFromFirestore(userId)
        }
    }
    private fun loadUserFromFirestore(userId: String) {
        viewModelScope.launch {
            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = UserModel.fromJson(document)
                        _userState.value = user
                    }
                }
                .addOnFailureListener {
//                    Log.e("ProfileViewModel", "Error fetching user")
                }
        }
    }
    private val repository = SwipeListRepository()
    private val auth = FirebaseAuth.getInstance()

    // State for available users
    val availableUsers = mutableStateListOf<UserProfile>()

    // State for loading status
    val isLoading = mutableStateOf(false)

    // State for error messages
    val errorMessage = mutableStateOf<String?>(null)

    init {
        loadAvailableUsers()
    }

    fun loadAvailableUsers() {

        isLoading.value = true
        errorMessage.value = null

        repository.getAvailableUsers(currentUser?.uid.toString()) { users ->
            availableUsers.clear()
            availableUsers.addAll(users)
            isLoading.value = false

            if (users.isEmpty()) {
                errorMessage.value = "No available users found"
            }
        }
    }

    fun likeUser(userId: String, onComplete: (Boolean) -> Unit = {}) {
        repository.like(currentUser?.uid.toString(), userId) { success ->
            if (success) {
                Log.i("Like user id:", userId);
            }
            onComplete(success)
        }
    }

    fun missUser(userId: String, onComplete: (Boolean) -> Unit = {}) {
        repository.miss(currentUser?.uid.toString(), userId) { success ->
            if (success) {
                Log.i("Miss user id:", userId);
            }
            onComplete(success)
        }
    }

    fun undoUser(userId: String, onComplete: (Boolean) -> Unit = {}) {
        repository.deleteMiss(currentUser?.uid.toString(), userId) { success ->
            if (success) {
                Log.i("Undo user id:", userId);
            }
            onComplete(success)
        }
    }
}