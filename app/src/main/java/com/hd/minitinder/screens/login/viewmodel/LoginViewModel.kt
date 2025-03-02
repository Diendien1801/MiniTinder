package com.hd.minitinder.screens.login.viewmodel

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.data.repositories.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _loginSuccess = mutableStateOf(false)
    val loginSuccess: State<Boolean> = _loginSuccess

    private val userRepository: UserRepository = UserRepository()
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _errorMessage.value = "Email and password are required!"
            return
        }

        _isLoading.value = true
        _errorMessage.value = ""

        auth.signInWithEmailAndPassword(_email.value, _password.value)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _loginSuccess.value = true
                    _errorMessage.value = "Login successful!"
                } else {
                    _errorMessage.value = task.exception?.message ?: "Login failed!"
                }
            }
    }

    fun loginWithFacebook(activity: Activity, callbackManager: CallbackManager) {
        _isLoading.value = true
        _errorMessage.value = ""

        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email", "public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                _isLoading.value = false
                _errorMessage.value = "Facebook login canceled!"
            }

            override fun onError(error: FacebookException) {
                _isLoading.value = false
                _errorMessage.value = error.message ?: "Facebook login failed!"
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    firebaseUser?.let {
                        userRepository.checkAndSaveUser(it)
                    }
                    _loginSuccess.value = true
                    _errorMessage.value = "Facebook login successful!"
                } else {
                    _errorMessage.value = task.exception?.message ?: "Authentication failed!"
                }
            }
    }



}
