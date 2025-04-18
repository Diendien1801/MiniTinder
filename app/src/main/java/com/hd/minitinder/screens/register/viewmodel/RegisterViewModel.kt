package com.hd.minitinder.screens.register.viewmodel

import android.content.Context
import android.util.Base64
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.data.repositories.UserRepository
import kotlinx.coroutines.launch
import java.security.KeyPairGenerator
import javax.inject.Inject

class RegisterViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isLoading = mutableStateOf<Boolean>(false)
    var isLoading: State<Boolean> = _isLoading

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

    private val _user = mutableStateOf<UserModel>(UserModel(

    ))
    var user: State<UserModel> = _user

    fun updateUserModelToDatabase() {
        userRepository.updateUserToDatabase(user.value)
    }

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



    fun register(context: Context, onResult: (Boolean) -> Unit) {
        var isValid = true
        _isLoading.value = true
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
                            // Tạo khóa RSA
                            //val keyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
                            // Lưu khóa private vào share preferences
                            //SharedPreferencesManager.savePrivateKey( context, firebaseUser.uid, keyPair.private.encoded)
                            // Lưu khóa RSA vào Firestore
                            //val publicKey = Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
                            val user = UserModel(
                                id = firebaseUser.uid,
                                imageUrls = listOf(),
                                isPremium = false,
                                //publicKey = publicKey,
                            )

                            userRepository.saveUserToFirestore(user,
                                onSuccess = {
                                    _errorMessage.value = "Registration successful!"
                                    onResult(true)
                                    _isLoading.value = false
                                },
                                onFailure = { exception ->
                                    _errorMessage.value = "Failed to save user: ${exception.message}"
                                    onResult(false)
                                    _isLoading.value = false
                                }
                            )
                        }
                    } else {
                        _errorMessage.value = task.exception?.message ?: "Registration failed!"
                        onResult(false)
                        _isLoading.value = false
                    }
                }
        }
    }


}
