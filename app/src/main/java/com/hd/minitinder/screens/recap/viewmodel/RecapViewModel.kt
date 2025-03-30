package com.hd.minitinder.screens.recap.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ThumbUp
import androidx.lifecycle.ViewModel
import com.hd.minitinder.screens.recap.model.RecapData
import com.hd.minitinder.screens.recap.model.RecapPeriod
import com.hd.minitinder.screens.recap.model.TopConversation
import com.hd.minitinder.screens.recap.model.UserInsight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecapViewModel : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(RecapPeriod.Monthly)
    val selectedPeriod: StateFlow<RecapPeriod> = _selectedPeriod

    private val _recapData = MutableStateFlow(getInitialRecapData())
    val recapData: StateFlow<RecapData> = _recapData

    private val _userInsights = MutableStateFlow(generateUserInsights())
    val userInsights: StateFlow<List<UserInsight>> = _userInsights

    fun selectPeriod(period: RecapPeriod) {
        _selectedPeriod.value = period
        _recapData.value = when (period) {
            RecapPeriod.Weekly -> getWeeklyRecapData()
            RecapPeriod.Monthly -> getInitialRecapData()
            RecapPeriod.Yearly -> getYearlyRecapData()
        }
        _userInsights.value = generateUserInsights()
    }

    private fun getInitialRecapData(): RecapData {
        return RecapData(
            totalSwipes = 428,
            rightSwipes = 134,
            leftSwipes = 294,
            matches = 32,
            matchRate = 23.9f,
            messagesSent = 186,
            messagesPerMatch = 5.8f,
            topConversations = listOf(
                TopConversation("Alex", 42, "3 days"),
                TopConversation("Jamie", 28, "2 days"),
                TopConversation("Taylor", 22, "1 day")
            ),
            averageActivityMinutes = 24,
            commonInterests = mapOf(
                "Travel" to 15,
                "Music" to 12,
                "Food" to 9,
                "Movies" to 8,
                "Hiking" to 7,
                "Photography" to 5
            )
        )
    }

    private fun getWeeklyRecapData(): RecapData {
        return RecapData(
            totalSwipes = 112,
            rightSwipes = 38,
            leftSwipes = 74,
            matches = 9,
            matchRate = 23.7f,
            messagesSent = 42,
            messagesPerMatch = 4.7f,
            topConversations = listOf(
                TopConversation("Sam", 18, "2 days"),
                TopConversation("Jordan", 14, "1 day"),
                TopConversation("Casey", 10, "1 day")
            ),
            averageActivityMinutes = 22,
            commonInterests = mapOf(
                "Travel" to 4,
                "Music" to 3,
                "Food" to 3,
                "Sports" to 2,
                "Art" to 1
            )
        )
    }

    private fun getYearlyRecapData(): RecapData {
        return RecapData(
            totalSwipes = 5842,
            rightSwipes = 1963,
            leftSwipes = 3879,
            matches = 428,
            matchRate = 21.8f,
            messagesSent = 2485,
            messagesPerMatch = 5.8f,
            topConversations = listOf(
                TopConversation("Morgan", 186, "2 weeks"),
                TopConversation("Riley", 142, "10 days"),
                TopConversation("Avery", 124, "8 days"),
                TopConversation("Drew", 98, "5 days")
            ),
            averageActivityMinutes = 26,
            commonInterests = mapOf(
                "Travel" to 124,
                "Music" to 118,
                "Food" to 98,
                "Movies" to 86,
                "Sports" to 72,
                "Art" to 65,
                "Books" to 56,
                "Photography" to 48,
                "Hiking" to 42
            )
        )
    }

    private fun generateUserInsights(): List<UserInsight> {
        val period = _selectedPeriod.value.name.lowercase()
        val data = _recapData.value

        val swipeRatio = data.rightSwipes.toFloat() / data.totalSwipes.toFloat()
        val responsiveness = data.messagesPerMatch
        val consistency = data.averageActivityMinutes
        val matchRate = data.matchRate

        return listOf(
            UserInsight(
                title = "Selectivity",
                description = when {
                    swipeRatio < 0.05 -> "You almost never swipe right. Are you too picky?"
                    swipeRatio < 0.1 -> "Extremely selective in your $period swiping habits."
                    swipeRatio < 0.15 -> "You have very high standards for matches."
                    swipeRatio < 0.2 -> "You're quite selective when choosing who to swipe right on."
                    swipeRatio < 0.3 -> "You take your time to find the right match."
                    swipeRatio < 0.4 -> "You’re moderately selective in making connections."
                    swipeRatio < 0.5 -> "You have a balanced approach to swiping."
                    swipeRatio < 0.6 -> "You’re fairly open to making new connections."
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
                    responsiveness < 4 -> "You reply occasionally but don’t chat much."
                    responsiveness < 6 -> "You engage but keep your messages short."
                    responsiveness < 8 -> "You respond regularly and maintain conversations."
                    responsiveness < 10 -> "You're quite chatty with your matches!"
                    responsiveness < 12 -> "You’re an engaging conversationalist."
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
                    consistency < 20 -> "You’re somewhat active but not daily."
                    consistency < 25 -> "You engage consistently but not excessively."
                    consistency < 30 -> "You maintain a good balance of activity."
                    consistency < 40 -> "You’re highly active on the app."
                    consistency < 50 -> "You spend a lot of time connecting with others!"
                    consistency < 60 -> "You're almost always active during the $period."
                    else -> "You're on the app all the time! True dedication."
                },
                icon = Icons.Default.Schedule
            ),
            UserInsight(
                title = "Match Success",
                description = when {
                    matchRate < 5 -> "You’re struggling to get matches. Maybe update your profile?"
                    matchRate < 10 -> "You get a few matches, but there's room to improve."
                    matchRate < 15 -> "Your profile is starting to attract attention."
                    matchRate < 20 -> "You have a decent match rate. Keep engaging!"
                    matchRate < 25 -> "You connect with a fair number of potential matches."
                    matchRate < 30 -> "Your profile is quite attractive to others!"
                    matchRate < 35 -> "You're getting strong interest from matches!"
                    matchRate < 40 -> "You have a high match rate! People like what they see."
                    matchRate < 50 -> "You're one of the most popular profiles!"
                    else -> "Your match rate is through the roof! You’re a superstar!"
                },
                icon = Icons.Default.Favorite
            )
        )
    }

}