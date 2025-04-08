package com.hd.minitinder.screens.recap.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

enum class RecapPeriod {
    Daily,
    Weekly,
    Monthly
}

data class TopConversation(
    val name: String,
    val messageCount: Int
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

class RecapRepository {
    private val db = FirebaseFirestore.getInstance()

    private fun getPeriodTime(period: RecapPeriod): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        val endTime = System.currentTimeMillis() // current time as end time

        // Calculate start time based on the period
        val startTime = when (period) {
            RecapPeriod.Daily -> {
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                calendar.timeInMillis
            }
            RecapPeriod.Weekly -> {
                calendar.add(Calendar.WEEK_OF_YEAR, -1)
                calendar.timeInMillis
            }
            RecapPeriod.Monthly -> {
                calendar.add(Calendar.MONTH, -1)
                calendar.timeInMillis
            }
        }

        return Pair(startTime, endTime)
    }

    // Function to get total swipes and matches for a specific period
    fun getRecapData(userId: String, period: RecapPeriod, onResult: (RecapData) -> Unit) {
        val (startTime, endTime) = getPeriodTime(period)

        // Get total likes (quẹt phải)
        db.collection("likes")
            .whereEqualTo("idUser1", userId)
            .whereGreaterThanOrEqualTo("timestamp", startTime)
            .whereLessThanOrEqualTo("timestamp", endTime)
            .get()
            .addOnSuccessListener { likesResult ->
                val rightSwipes = likesResult.size()

                db.collection("matches")
                    .whereEqualTo("idUser1", userId)
                    .whereGreaterThanOrEqualTo("timestamp", startTime)
                    .whereLessThanOrEqualTo("timestamp", endTime)
                    .get()
                    .addOnSuccessListener { matchResult1 ->
                        db.collection("matches")
                            .whereEqualTo("idUser2", userId)
                            .whereGreaterThanOrEqualTo("timestamp", startTime)
                            .whereLessThanOrEqualTo("timestamp", endTime)
                            .get()
                            .addOnSuccessListener { matchResult2 ->
                                val matches = matchResult1.size() + matchResult2.size()

                                // Get total misses (quẹt trái)
                                db.collection("misses")
                                    .whereEqualTo("idUser1", userId)
                                    .whereGreaterThanOrEqualTo("timestamp", startTime)
                                    .whereLessThanOrEqualTo("timestamp", endTime)
                                    .get()
                                    .addOnSuccessListener { missesResult ->
                                        val leftSwipes = missesResult.size()
                                        val messagesSent = 0 // Example, you need to calculate based on your app's data
                                        val messagesPerMatch = if (matches > 0) messagesSent / matches.toFloat() else 0f
                                        val topConversations = listOf<TopConversation>() // Get top conversations if needed
                                        val averageActivityMinutes = 0 // Calculate based on activity if needed
                                        val commonInterests = mapOf<String, Int>() // Define common interests if needed

                                        val recapData = RecapData(
                                            totalSwipes = rightSwipes + leftSwipes + matches,
                                            rightSwipes = rightSwipes + matches,
                                            leftSwipes = leftSwipes,
                                            matches = matches,
                                            matchRate = if (rightSwipes > 0) matches / rightSwipes.toFloat() else 0f,
                                            messagesSent = messagesSent,
                                            messagesPerMatch = messagesPerMatch,
                                            topConversations = topConversations,
                                            averageActivityMinutes = averageActivityMinutes,
                                            commonInterests = commonInterests
                                        )
                                        onResult(recapData)
                                    }
                            }
                    }
            }
    }
}