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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class UserProfile(
    val id: String,
    val name: String,
    val age: Int,
    val imageUrls: List<String>,
    val tags: List<String>,
    val address: String,
    val occupation: String,
    val bio: String,
    val gender: String
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
    private fun calculateAge(dobString: String): Int {
        try {
            val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val dob = dateFormat.parse(dobString) ?: return 0

            val dobCalendar = Calendar.getInstance().apply { time = dob }
            val currentCalendar = Calendar.getInstance()

            var age = currentCalendar.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)

            // Adjust age if birthday hasn't occurred yet this year
            if (currentCalendar.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--
            }

            return age
        } catch (e: Exception) {
            return 0
        }
    }
    fun loadAvailableUsers() {
        isLoading.value = true
        errorMessage.value = null

        Log.d("SwipeViewModel", "Fetching available users...")

        repository.getAvailableUsers(currentUser?.uid.toString()) { users ->
            availableUsers.clear()

            // Lọc các user có cùng giới tính với user hiện tại
            val filteredUsers = users.filter { it.gender != _userState.value.gender }

            val userAge = calculateAge(_userState.value.dob)

            // Sắp xếp các user theo độ tuổi gần nhất và theo số lượng tags giống nhau
            val sortedUsers = filteredUsers.sortedWith { user1, user2 ->
                // Sắp xếp theo độ tuổi gần nhất
                val ageDifference1 = Math.abs(user1.age - userAge)
                val ageDifference2 = Math.abs(user2.age - userAge)
                val ageComparison = ageDifference1.compareTo(ageDifference2)

                if (ageComparison != 0) {
                    ageComparison
                } else {
                    // Nếu độ tuổi bằng nhau, so sánh theo số lượng tags giống nhau
                    val commonTags1 = user1.tags.intersect(_userState.value.interests).size
                    val commonTags2 = user2.tags.intersect(_userState.value.interests).size
                    commonTags2.compareTo(commonTags1) // Ưu tiên người có nhiều tags giống nhau hơn
                }
            }

            // Thêm các user đã sắp xếp vào danh sách availableUsers
            availableUsers.addAll(sortedUsers)

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