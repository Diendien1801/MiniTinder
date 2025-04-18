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
import com.google.firebase.messaging.FirebaseMessaging
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.data.repositories.UserRepository
import com.hd.minitinder.service.MyFirebaseMessagingService
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.KeyPairGenerator

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _currentUser = mutableStateOf<FirebaseUser?>(null)
    var currentUser: State<FirebaseUser?> = _currentUser


    private val _email = mutableStateOf("")
    var email: State<String> = _email

    private val _password = mutableStateOf("")
    var password: State<String> = _password

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
    fun logout() {
        // --- [Trước khi logout] ---
        val accessTokenBefore = AccessToken.getCurrentAccessToken()
        val profileBefore = Profile.getCurrentProfile()

        Log.d("FacebookLogout", "=== Before Logout ===")
        if (accessTokenBefore != null && !accessTokenBefore.isExpired) {
            Log.d("FacebookLogout", "AccessToken: ${accessTokenBefore.token}")
            Log.d("FacebookLogout", "UserId: ${accessTokenBefore.userId}")
            Log.d("FacebookLogout", "ApplicationId: ${accessTokenBefore.applicationId}")
            Log.d("FacebookLogout", "Permissions: ${accessTokenBefore.permissions}")
            Log.d("FacebookLogout", "DeclinedPermissions: ${accessTokenBefore.declinedPermissions}")
            Log.d("FacebookLogout", "Expires: ${accessTokenBefore.expires}")
            Log.d("FacebookLogout", "LastRefresh: ${accessTokenBefore.lastRefresh}")
        } else {
            Log.d("FacebookLogout", "No valid Facebook access token found.")
        }

        if (profileBefore != null) {
            Log.d("FacebookLogout", "Profile Name: ${profileBefore.name}")
            Log.d("FacebookLogout", "Profile Id: ${profileBefore.id}")
            Log.d("FacebookLogout", "Profile LinkUri: ${profileBefore.linkUri}")
            Log.d("FacebookLogout", "Profile PictureUri: ${profileBefore.getProfilePictureUri(100, 100)}")
        } else {
            Log.d("FacebookLogout", "No Facebook profile found.")
        }

        // --- [Tiến hành logout] ---
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
        AccessToken.setCurrentAccessToken(null)
        Profile.setCurrentProfile(null)

        // --- [Sau khi logout] ---
        val accessTokenAfter = AccessToken.getCurrentAccessToken()
        val profileAfter = Profile.getCurrentProfile()

        Log.d("FacebookLogout", "=== After Logout ===")
        if (accessTokenAfter != null && !accessTokenAfter.isExpired) {
            Log.d("FacebookLogout", "AccessToken: ${accessTokenAfter.token}")
        } else {
            Log.d("FacebookLogout", "AccessToken is null or expired")
        }

        if (profileAfter != null) {
            Log.d("FacebookLogout", "Profile Name: ${profileAfter.name}")
        } else {
            Log.d("FacebookLogout", "Profile is null")
        }

        // --- [Cập nhật trạng thái UI] ---
        _currentUser.value = null
        _loginSuccess.value = false
        _errorMessage.value = "You have been logged out."
        Log.d("LoginViewModel", "Logout successful")
    }
    fun login(context: Context) {
        Log.d("LoginViewModel", "Login started with email: ${email.value}")

        if (email.value.isBlank() || password.value.isBlank()) {
            _errorMessage.value = "Email and password are required!"
            Log.e("LoginViewModel", "Validation failed: Email or password is blank")
            return
        }

        _isLoading.value = true
        _errorMessage.value = ""

        auth.signInWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _currentUser.value = auth.currentUser
                    _loginSuccess.value = true
                    _errorMessage.value = "Login successful!"
                    Log.d("LoginViewModel", "Login successful! User: ${auth.currentUser?.uid}")
                    // đăng ký 1 token FCM
                    viewModelScope.launch {
                        try {
                            // Cập nhật thông tin khác nếu cần


                            val userDoc = Firebase.firestore.collection("users")
                                .document(auth.currentUser?.uid?:"").get().await()
                            if(!userDoc.exists() || userDoc.getString("fcmToken").isNullOrEmpty())
                            {
                                registerFCMToken()
                            }

                            if (!userDoc.exists() || userDoc.getString("publicKey").isNullOrEmpty()) {
                                auth.currentUser?.let { createAndSaveKeyPair(it, context) }
                            } else {
                                Log.d("LoginViewModel", "User already has a key pair.")
                            }



                            _loginSuccess.value = true
                            _errorMessage.value = "Facebook login successful!"
                        } catch (e: Exception) {
                            Log.e("LoginViewModel", "Error during login process: ${e.message}")
                            _errorMessage.value = "Login failed: ${e.message}"
                        }
                    }
                } else {
                    _errorMessage.value = task.exception?.message ?: "Login failed!"
                    Log.e("LoginViewModel", "Login failed: ${task.exception?.message}")
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
                // đăng ký 1 token FCM
                //registerFCMToken()
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

    fun handleFacebookAccessToken(token: AccessToken, context: Context) {
        _isLoadingFace.value = true

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _isLoadingFace.value = false

                if (task.isSuccessful) {
                    _currentUser.value = auth.currentUser
                    val user = _currentUser.value

                    if (user != null) {
                        viewModelScope.launch {
                            try {
                                // Cập nhật thông tin khác nếu cần
                                userRepository.checkAndSaveUser(user)

                                val userDoc = Firebase.firestore.collection("users")
                                    .document(user.uid).get().await()
                                if(!userDoc.exists() || userDoc.getString("fcmToken").isNullOrEmpty())
                                {
                                    registerFCMToken()
                                }

                                if (!userDoc.exists() || userDoc.getString("publicKey").isNullOrEmpty()) {
                                    createAndSaveKeyPair(user, context)
                                } else {
                                    Log.d("LoginViewModel", "User already has a key pair.")
                                }



                                _loginSuccess.value = true
                                _errorMessage.value = "Facebook login successful!"
                            } catch (e: Exception) {
                                Log.e("LoginViewModel", "Error during login process: ${e.message}")
                                _errorMessage.value = "Login failed: ${e.message}"
                            }
                        }
                    }
                } else {
                    _errorMessage.value = task.exception?.message ?: "Authentication failed!"
                }
            }
    }


    suspend private fun createAndSaveKeyPair(user: FirebaseUser, context: Context) {
        // Tạo cặp khóa RSA mới
        val keyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
        // Lưu private key vào thiết bị
        SharedPreferencesManager.savePrivateKey(context, user.uid, keyPair.private.encoded)
        // Chuyển public key thành chuỗi Base64 để lưu lên Firestore
        val publicKeyString = Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
        val privateKeyString = Base64.encodeToString(keyPair.private.encoded, Base64.DEFAULT)
        val userDocRef = Firebase.firestore.collection("users").document(user.uid)
        val data = mapOf(
            "publicKey" to publicKeyString,
            "privateKey" to privateKeyString,
            )
        // Sử dụng set() để tạo document mới nếu chưa tồn tại
        userDocRef.set(data, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {
                Log.d("LoginViewModel", "Key pair created/updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("LoginViewModel", "Failed to update key pair: ${e.message}")
            }

    }

    // đăng ký token FCM
    fun registerFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Lấy token thất bại", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("FCM", "FCM Token: $token")
                MyFirebaseMessagingService().saveUserFCMToken(auth.currentUser?.uid.toString(), token)
            }
    }


}
