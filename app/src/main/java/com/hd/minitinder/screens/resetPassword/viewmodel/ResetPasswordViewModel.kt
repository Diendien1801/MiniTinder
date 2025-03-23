package com.hd.minitinder.screens.resetpassword.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ResetPasswordViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var isLoading = mutableStateOf(false) // Sử dụng mutableStateOf thay vì MutableState<Boolean>

    fun resetPassword(email: String, onResult: (Boolean, String) -> Unit) {
        if (email.isBlank()) {
            onResult(false, "Email cannot be empty")
            return
        }

        isLoading.value = true // Bắt đầu loading

        viewModelScope.launch {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    isLoading.value = false // Kết thúc loading

                    if (task.isSuccessful) {
                        onResult(true, "Reset link sent! Check your email.")
                    } else {
                        onResult(false, task.exception?.localizedMessage ?: "Reset failed")
                    }
                }
        }
    }

}
