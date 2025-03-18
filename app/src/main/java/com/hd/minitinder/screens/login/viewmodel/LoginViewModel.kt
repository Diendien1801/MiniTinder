package com.hd.minitinder.screens.login.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Base64
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.data.repositories.UserRepository
import kotlinx.coroutines.launch
import java.security.KeyPairGenerator

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _currentUser = mutableStateOf<FirebaseUser?>(null)
    var currentUser: State<FirebaseUser?> = _currentUser


    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isLoadingFace = mutableStateOf(false)
    val isLoadingFace: State<Boolean> = _isLoadingFace

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
                    _currentUser.value = auth.currentUser  // Cập nhật user hiện tại
                    _loginSuccess.value = true
                    _errorMessage.value = "Login successful!"
                } else {
                    _errorMessage.value = task.exception?.message ?: "Login failed!"
                }
            }
    }

    fun loginWithFacebook(activity: Activity, callbackManager: CallbackManager) {
        _isLoadingFace.value = true
        _errorMessage.value = ""

        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email", "public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken, activity)
            }

            override fun onCancel() {
                _isLoadingFace.value = false
                _errorMessage.value = "Facebook login canceled!"
            }

            override fun onError(error: FacebookException) {
                _isLoadingFace.value = false
                _errorMessage.value = error.message ?: "Facebook login failed!"
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken, context: Context) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _isLoadingFace.value = false
                if (task.isSuccessful) {
                    _currentUser.value = auth.currentUser
                    Log.d("LoginViewModel", "User ID: ${_currentUser.value?.uid}")
                    val user = _currentUser.value
                    if (user != null) {
                        val userDocRef = Firebase.firestore.collection("users").document(user.uid)
                        userDocRef.get()
                            .addOnSuccessListener { document ->
                                if (!document.exists() || document.getString("publicKey").isNullOrEmpty()) {
                                    // Nếu document chưa tồn tại hoặc publicKey trống, tạo key pair mới
                                    createAndSaveKeyPair(user, context)
                                } else {
                                    Log.d("LoginViewModel", "User already has a key pair.")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("LoginViewModel", "Error checking user key: ${e.message}")
                            }
                        // Gọi hàm checkAndSaveUser nếu cần cập nhật thông tin khác của người dùng
                        userRepository.checkAndSaveUser(user)
                    }
                    _loginSuccess.value = true
                    _errorMessage.value = "Facebook login successful!"
                } else {
                    _errorMessage.value = task.exception?.message ?: "Authentication failed!"
                }
            }
    }

    private fun createAndSaveKeyPair(user: FirebaseUser, context: Context) {
        // Tạo cặp khóa RSA mới
        val keyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
        // Lưu private key vào thiết bị
        SharedPreferencesManager.savePrivateKey(context, user.uid, keyPair.private.encoded)
        // Chuyển public key thành chuỗi Base64 để lưu lên Firestore
        val publicKeyString = Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
        val userDocRef = Firebase.firestore.collection("users").document(user.uid)
        val data = mapOf("publicKey" to publicKeyString)
        // Sử dụng set() để tạo document mới nếu chưa tồn tại
        userDocRef.set(data, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {
                Log.d("LoginViewModel", "Key pair created/updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("LoginViewModel", "Failed to update key pair: ${e.message}")
            }

    }




}
