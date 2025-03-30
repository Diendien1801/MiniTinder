package com.hd.minitinder.screens.recap.model

import androidx.compose.ui.graphics.vector.ImageVector

enum class RecapPeriod {
    Weekly,
    Monthly,
    Yearly
}

data class TopConversation(
    val name: String,
    val messageCount: Int,
    val duration: String
)

data class UserInsight(
    val title: String,
    val description: String,
    val icon: ImageVector
)

data class RecapData(
    val totalSwipes: Int,
    val rightSwipes: Int,
    val leftSwipes: Int,
    val matches: Int,
    val matchRate: Float,
    val messagesSent: Int,
    val messagesPerMatch: Float,
    val topConversations: List<TopConversation>,
    val averageActivityMinutes: Int,
    val commonInterests: Map<String, Int>
)