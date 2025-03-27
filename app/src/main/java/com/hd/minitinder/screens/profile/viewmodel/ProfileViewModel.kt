package com.hd.minitinder.screens.profile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _userState = MutableStateFlow(UserModel())
    val userState: StateFlow<UserModel> = _userState

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    val uid: String? = auth.currentUser?.uid
    val userRepository = UserRepository()
    init {
//        Log.d("ProfileViewModel", "Current User UID: $uid")
        uid?.let { userId ->
            loadUserFromFirestore(userId)
        }
    }



    /**
     * Hàm giả lập lấy dữ liệu người dùng từ Firestore.
     */
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

    /**
     * Lưu dữ liệu người dùng lên Firestore.
     */
    fun updateImage(newImageUrls: List<String>) {
        viewModelScope.launch {
            userRepository.updateUserImage(
                _userState.value.id,
                newImageUrls,
                onSuccess = {
                    Log.d("ProfileViewModel", "Cập nhật ảnh thành công")
                    _userState.value = _userState.value.copy(imageUrls = newImageUrls)
                },
                onFailure = { error ->
                    Log.e("ProfileViewModel", "Lỗi khi cập nhật ảnh: $error")
                }
            )
        }
    }

    /**
     * Lưu dữ liệu người dùng lên Firestore.
     */
    fun saveUserToFirestore() {
        viewModelScope.launch {
            val user = _userState.value
            firestore.collection("users")
                .document(user.id)
                .set(user.toJson())
                .addOnSuccessListener {
                    // Lưu thành công
                }
                .addOnFailureListener {
                    // Lỗi
                }
        }
    }

    // Các hàm cập nhật state khi người dùng chỉnh sửa
    fun onNameChange(newName: String) {
        _userState.value = _userState.value.copy(name = newName)
    }

    fun onGenderChange(newGender: String) {
        _userState.value = _userState.value.copy(gender = newGender)
    }

    fun onDobChange(newDob: String) {
        _userState.value = _userState.value.copy(dob = newDob)
    }

    fun onHometownChange(newHometown: String) {
        _userState.value = _userState.value.copy(hometown = newHometown)
    }

    fun onJobChange(newJob: String) {
        _userState.value = _userState.value.copy(job = newJob)
    }

    fun onHeightChange(newHeight: Double) {
        _userState.value = _userState.value.copy(height = newHeight)
    }

    fun onWeightChange(newWeight: Double) {
        _userState.value = _userState.value.copy(weight = newWeight)
    }

    fun onPhoneNumberChange(newPhone: String) {
        _userState.value = _userState.value.copy(phoneNumber = newPhone)
    }

    fun onBioChange(newBio: String) {
        _userState.value = _userState.value.copy(bio = newBio)
    }

    fun onIsPremiumChange(isPremium: Boolean) {
        _userState.value = _userState.value.copy(isPremium = isPremium)
    }

    fun onInterestsChange(newInterests: List<String>) {
        _userState.value = _userState.value.copy(interests = newInterests)
    }

    fun onImageChange(newImageUrls: List<String>) {
        _userState.value = _userState.value.copy(imageUrls = newImageUrls)
    }
}
