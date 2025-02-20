package com.hd.minitinder.screens.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _confirmPassword = mutableStateOf("")
    val confirmPassword: State<String> = _confirmPassword

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
    }

    fun register() {
        if (_email.value.isBlank() || _password.value.isBlank() || _confirmPassword.value.isBlank()) {
            _errorMessage.value = "All fields are required!"
            return
        }

        if (_password.value != _confirmPassword.value) {
            _errorMessage.value = "Passwords do not match!"
            return
        }

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _errorMessage.value = "Registration successful!"
                    } else {
                        _errorMessage.value = task.exception?.message ?: "Registration failed!"
                    }
                }
        }
    }
}
