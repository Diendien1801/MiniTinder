package com.hd.minitinder.screens.recap.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ThumbUp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hd.minitinder.screens.recap.model.RecapData
import com.hd.minitinder.screens.recap.model.RecapPeriod
import com.hd.minitinder.screens.recap.model.RecapRepository
import com.hd.minitinder.screens.recap.model.TopConversation
import com.hd.minitinder.screens.recap.model.UserInsight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import kotlinx.coroutines.launch

class RecapViewModel(private val repository: RecapRepository = RecapRepository()) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(RecapPeriod.Monthly)
    val selectedPeriod: StateFlow<RecapPeriod> = _selectedPeriod

    private val _recapData = MutableStateFlow(RecapData(
        totalSwipes = 0,
        rightSwipes = 0,
        leftSwipes = 0,
        matches = 0,
        matchRate = 0f,
        messagesSent = 0,
        messagesPerMatch = 0f,
        topConversations = emptyList(),
        averageActivityMinutes = 0,
        commonInterests = emptyMap()
    ))
    val recapData: StateFlow<RecapData> = _recapData

    private val _userInsights = MutableStateFlow<List<UserInsight>>(emptyList())
    val userInsights: StateFlow<List<UserInsight>> = _userInsights

    // Initialize data when ViewModel is created
    init {
        loadRecapData(_selectedPeriod.value)
    }

    // Giữ nguyên các phương thức đọc usage stats của bạn
    fun getAppUsageByInterval(
        context: Context,
        interval: Int,
        durationInMillis: Long
    ): Map<String, Long> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val endTime = System.currentTimeMillis()
        val startTime = endTime - durationInMillis

        val stats = usageStatsManager.queryUsageStats(interval, startTime, endTime)
        val result = mutableMapOf<String, Long>()

        stats?.forEach { stat ->
            val pkg = stat.packageName
            val time = stat.totalTimeInForeground
            if (time > 0) {
                result[pkg] = result.getOrDefault(pkg, 0L) + time
            }
        }

        return result.mapValues { it.value / 1000 / 60 } // convert to minutes
    }

    fun getDailyUsage(context: Context): Map<String, Long> {
        val oneDay = 1000L * 60 * 60 * 24
        return getAppUsageByInterval(context, UsageStatsManager.INTERVAL_DAILY, oneDay)
    }

    fun getWeeklyUsage(context: Context): Map<String, Long> {
        val oneWeek = 1000L * 60 * 60 * 24 * 7
        return getAppUsageByInterval(context, UsageStatsManager.INTERVAL_DAILY, oneWeek)
    }

    fun getMonthlyUsage(context: Context): Map<String, Long> {
        val oneMonth = 1000L * 60 * 60 * 24 * 30
        return getAppUsageByInterval(context, UsageStatsManager.INTERVAL_DAILY, oneMonth)
    }

    // Giữ nguyên phương thức lấy số phút sử dụng ứng dụng
    private fun getAppUsageMinutes(context: Context, period: RecapPeriod): Long {
        val packageName = context.packageName

        val usageMap = when (period) {
            RecapPeriod.Daily -> getDailyUsage(context)
            RecapPeriod.Weekly -> getWeeklyUsage(context)
            RecapPeriod.Monthly -> getMonthlyUsage(context)
        }

        return usageMap[packageName] ?: 0L
    }

    fun updateUsageMinutes(context: Context) {
        try {
            val minutes = getAppUsageMinutes(context, _selectedPeriod.value)
            Log.d("RecapViewModel", "Raw usage minutes: $minutes")

            // Cập nhật dữ liệu hiện có mà không thay đổi các trường khác
            val currentData = _recapData.value
            _recapData.value = currentData.copy(
                averageActivityMinutes = minutes.toInt()
            )

            Log.d("RecapViewModel", "Updated usage minutes: ${_recapData.value.averageActivityMinutes}")

            // Cập nhật insights sau khi có dữ liệu mới
            _userInsights.value = generateUserInsights()
        } catch (e: Exception) {
            Log.e("RecapViewModel", "Error updating usage minutes: ${e.message}", e)
        }
    }

    fun selectPeriod(context: Context, period: RecapPeriod) {
        _selectedPeriod.value = period
        loadRecapData(period)

        // Đảm bảo cập nhật usage minutes sau khi tải dữ liệu
        try {
            updateUsageMinutes(context)
        } catch (e: Exception) {
            Log.e("RecapViewModel", "Error in selectPeriod: ${e.message}", e)
        }
    }

    private fun loadRecapData(period: RecapPeriod) {
        viewModelScope.launch {
            try {
                repository.getRecapData(period) { data ->
                    // Lưu lại giá trị averageActivityMinutes hiện tại trước khi cập nhật
                    val currentMinutes = _recapData.value.averageActivityMinutes

                    _recapData.value = data.copy(
                        // Giữ nguyên giá trị minutes đã có
                        averageActivityMinutes = currentMinutes,
                        // Thêm các giá trị tạm thời cho các trường chưa có trong repository
                        topConversations = getTopConversationsForPeriod(period),
                        commonInterests = getCommonInterestsForPeriod(period)
                    )

                    _userInsights.value = generateUserInsights()

                    Log.d("RecapViewModel", "Loaded recap data for period: $period")
                }
            } catch (e: Exception) {
                Log.e("RecapViewModel", "Error loading recap data: ${e.message}", e)
            }
        }
    }

    // Các phương thức cung cấp dữ liệu tạm thời
    private fun getTopConversationsForPeriod(period: RecapPeriod): List<TopConversation> {
        return when (period) {
            RecapPeriod.Daily -> listOf(
                TopConversation("Sam", 15),
                TopConversation("Jordan", 8)
            )
            RecapPeriod.Weekly -> listOf(
                TopConversation("Sam", 38),
                TopConversation("Jordan", 24),
                TopConversation("Casey", 16)
            )
            RecapPeriod.Monthly -> listOf(
                TopConversation("Alex", 82),
                TopConversation("Jamie", 64),
                TopConversation("Taylor", 48)
            )
        }
    }

    private fun getCommonInterestsForPeriod(period: RecapPeriod): Map<String, Int> {
        return when (period) {
            RecapPeriod.Daily -> mapOf(
                "Travel" to 3,
                "Music" to 2,
                "Food" to 2
            )
            RecapPeriod.Weekly -> mapOf(
                "Travel" to 8,
                "Music" to 6,
                "Food" to 5,
                "Sports" to 4,
                "Art" to 3
            )
            RecapPeriod.Monthly -> mapOf(
                "Travel" to 22,
                "Music" to 18,
                "Food" to 14,
                "Movies" to 12,
                "Hiking" to 10,
                "Photography" to 8
            )
        }
    }

    private fun generateUserInsights(): List<UserInsight> {
        val period = _selectedPeriod.value.name.lowercase()
        val data = _recapData.value

        val swipeRatio = if (data.totalSwipes > 0) {
            data.rightSwipes.toFloat() / data.totalSwipes.toFloat()
        } else 0f

        val responsiveness = data.messagesPerMatch
        val consistency = data.averageActivityMinutes
        val matchRate = data.matchRate

        Log.d("RecapViewModel", "Generating insights with consistency: $consistency minutes")

        return listOf(
            UserInsight(
                title = "Selectivity",
                description = when {
                    swipeRatio < 0.05 -> "You almost never swipe right. Are you too picky?"
                    swipeRatio < 0.1 -> "Extremely selective in your $period swiping habits."
                    swipeRatio < 0.15 -> "You have very high standards for matches."
                    swipeRatio < 0.2 -> "You're quite selective when choosing who to swipe right on."
                    swipeRatio < 0.3 -> "You take your time to find the right match."
                    swipeRatio < 0.4 -> "You're moderately selective in making connections."
                    swipeRatio < 0.5 -> "You have a balanced approach to swiping."
                    swipeRatio < 0.6 -> "You're fairly open to making new connections."
                    swipeRatio < 0.8 -> "You explore a wide range of potential matches."
                    else -> "You're very open and swipe right on most profiles!"
                },
                icon = Icons.Default.ThumbUp
            ),
            UserInsight(
                title = "Engagement",
                description = when {
                    responsiveness == 0f -> "You rarely start or continue conversations."
                    responsiveness < 2 -> "You tend to be quite reserved in messaging."
                    responsiveness < 4 -> "You reply occasionally but don't chat much."
                    responsiveness < 6 -> "You engage but keep your messages short."
                    responsiveness < 8 -> "You respond regularly and maintain conversations."
                    responsiveness < 10 -> "You're quite chatty with your matches!"
                    responsiveness < 12 -> "You're an engaging conversationalist."
                    responsiveness < 15 -> "You love chatting and building connections!"
                    responsiveness < 20 -> "You initiate and keep conversations lively."
                    else -> "You're a messaging pro! Your engagement is top-tier."
                },
                icon = Icons.AutoMirrored.Filled.Chat
            ),
            UserInsight(
                title = "Consistency",
                description = when {
                    consistency < 5 -> "You barely use the app during the $period."
                    consistency < 10 -> "You check in once in a while, but not frequently."
                    consistency < 15 -> "You use the app occasionally without a set routine."
                    consistency < 20 -> "You're somewhat active but not daily."
                    consistency < 25 -> "You engage consistently but not excessively."
                    consistency < 30 -> "You maintain a good balance of activity."
                    consistency < 40 -> "You're highly active on the app."
                    consistency < 50 -> "You spend a lot of time connecting with others!"
                    consistency < 60 -> "You're almost always active during the $period."
                    else -> "You're on the app all the time! True dedication."
                },
                icon = Icons.Default.Schedule
            ),
            UserInsight(
                title = "Match Success",
                description = when {
                    matchRate < 5 -> "You're struggling to get matches. Maybe update your profile?"
                    matchRate < 10 -> "You get a few matches, but there's room to improve."
                    matchRate < 15 -> "Your profile is starting to attract attention."
                    matchRate < 20 -> "You have a decent match rate. Keep engaging!"
                    matchRate < 25 -> "You connect with a fair number of potential matches."
                    matchRate < 30 -> "Your profile is quite attractive to others!"
                    matchRate < 35 -> "You're getting strong interest from matches!"
                    matchRate < 40 -> "You have a high match rate! People like what they see."
                    matchRate < 50 -> "You're one of the most popular profiles!"
                    else -> "Your match rate is through the roof! You're a superstar!"
                },
                icon = Icons.Default.Favorite
            )
        )
    }
}