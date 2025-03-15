package com.hd.minitinder.screens.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.hd.minitinder.data.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _userState = MutableStateFlow(UserModel())
    val userState: StateFlow<UserModel> = _userState

    private val firestore = FirebaseFirestore.getInstance()

    init {
        // Giả sử ta load user với id = "abc" (hoặc lấy từ session, v.v.)
        loadUserFromFirestore("abc")
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
                    // Xử lý lỗi
                }
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

    // Giả lập interests, có thể tuỳ chỉnh
    fun onInterestsChange(newInterests: List<String>) {
        _userState.value = _userState.value.copy(interests = newInterests)
    }
}
