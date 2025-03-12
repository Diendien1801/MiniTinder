package com.hd.minitinder.screens.history.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.screens.history.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel :ViewModel() {
    val categories = listOf("All", "Match", "Block", "Like")

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory

    var userId = FirebaseAuth.getInstance().currentUser?.uid ?: "8cF8maCFMvQ9apkZfiRbP1yWPNt2"
    val historyRepository : HistoryRepository = HistoryRepository()
    private val _likedUsers = MutableStateFlow<List<UserModel>>(emptyList())
    val likedUsers: StateFlow<List<UserModel>> = _likedUsers

    private val _matchedUsers = MutableStateFlow<List<UserModel>>(emptyList())
    val matchedUsers: StateFlow<List<UserModel>> = _matchedUsers

    private val _blockedUsers = MutableStateFlow<List<UserModel>>(emptyList())
    val blockedUsers: StateFlow<List<UserModel>> = _blockedUsers

    private val _allActivities = MutableStateFlow<List<UserModel>>(emptyList())
    var allActivities: StateFlow<List<UserModel>> = _allActivities


    val filteredUsers = combine(_selectedCategory, _likedUsers, _matchedUsers, _blockedUsers) { category, liked, matched, blocked ->
        when (category) {
            "Match" -> matched
            "Block" -> blocked
            "Like" -> liked
            else -> liked + matched + blocked
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    init {
        Log.d("HistoryViewModel", "Khởi tạo ViewModel, bắt đầu fetch dữ liệu liked users")
        viewModelScope.launch {
            fetchLikedUsers(userId)
            fetchMatchedUsers(userId)
            _allActivities.value = likedUsers.value + matchedUsers.value
        }
    }

        private fun fetchMatchedUsers(userId: String) {
            viewModelScope.launch {
                try {
                    Log.d("HistoryViewModel", "Bắt đầu lấy danh sách matched users cho userId: $userId")
                    val users = historyRepository.getMatchedUsers(userId) // Hàm truy vấn Firestore đã viết trước đó
                    Log.d("HistoryViewModel", "Lấy danh sách matched users thành công, số lượng: ${users.size}")
                    _matchedUsers.value = users

                }
                catch (e: Exception) {
                    Log.e("HistoryViewModel", "Lỗi khi fetch matched users: ${e.message}", e)
                }
            }
        }
        private fun fetchLikedUsers(userId: String) {
            viewModelScope.launch {
                try {
                    Log.d("HistoryViewModel", "Bắt đầu lấy danh sách liked users cho userId: $userId")

                    val users = historyRepository.getLikeActivity(userId) // Hàm truy vấn Firestore đã viết trước đó

                    Log.d("HistoryViewModel", "Lấy danh sách liked users thành công, số lượng: ${users.size}")
                    _likedUsers.value = users

                } catch (e: Exception) {
                    Log.e("HistoryViewModel", "Lỗi khi fetch liked users: ${e.message}", e)
                }
            }
        }

    fun updateCategory(category: String) {
        _selectedCategory.value = category // Cập nhật giá trị
    }


}