package com.hd.minitinder.screens.register.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.data.repositories.UserRepository
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

    private val _emailError = mutableStateOf("")
    val emailError: State<String> = _emailError

    private val _passwordError = mutableStateOf("")
    val passwordError: State<String> = _passwordError

    private val _confirmPasswordError = mutableStateOf("")
    val confirmPasswordError: State<String> = _confirmPasswordError
    private val userRepository: UserRepository = UserRepository()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _emailError.value = "" // Reset lỗi khi người dùng nhập
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _passwordError.value = ""
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        _confirmPasswordError.value = ""
    }



    fun register(onResult: (Boolean) -> Unit) {
        var isValid = true

        if (_email.value.isBlank()) {
            _emailError.value = "Email is required!"
            isValid = false
        }

        if (_password.value.isBlank()) {
            _passwordError.value = "Password is required!"
            isValid = false
        }

        if (_confirmPassword.value.isBlank()) {
            _confirmPasswordError.value = "Confirm Password is required!"
            isValid = false
        }

        if (_password.value != _confirmPassword.value) {
            _confirmPasswordError.value = "Passwords do not match!"
            isValid = false
        }

        if (!isValid) return

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(_email.value, _password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result?.user
                        if (firebaseUser != null) {
                            val user = UserModel(
                                id = firebaseUser.uid,
                                imageUrls = listOf(),
                                isPremium = false
                            )

                            userRepository.saveUserToFirestore(user,
                                onSuccess = {
                                    _errorMessage.value = "Registration successful!"
                                    onResult(true)
                                },
                                onFailure = { exception ->
                                    _errorMessage.value = "Failed to save user: ${exception.message}"
                                    onResult(false)
                                }
                            )
                        }
                    } else {
                        _errorMessage.value = task.exception?.message ?: "Registration failed!"
                        onResult(false)
                    }
                }
        }
    }


}
