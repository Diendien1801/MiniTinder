import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TinderGoldViewModel : ViewModel() {
    private val _likedUsers = MutableStateFlow<List<UserModel>>(emptyList())
    val likedUsers = _likedUsers.asStateFlow() // Expose dưới dạng StateFlow để dùng trong Compose

    private val userRepository = UserRepository()
    private val _isPremium = MutableStateFlow<Boolean?>(null)
    val isPremium = _isPremium.asStateFlow() // Không gán lại giá trị!

    init {
        Log.d("init1", "init Call")

        val userId = Firebase.auth.currentUser?.uid ?: "8cF8maCFMvQ9apkZfiRbP1yWPNt2"

        // Lấy giá trị premium đúng cách
        viewModelScope.launch {
            _isPremium.value = userRepository.isPremium(userId)
        }



        // Lấy danh sách liked users
        viewModelScope.launch {
            userRepository.getLikedUsers(userId) { users ->
                _likedUsers.value = users // Cập nhật danh sách
                Log.d("likedUsers", "Fetched ${users.size} users")
            }
        }
    }
}
