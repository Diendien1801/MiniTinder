package com.hd.minitinder.screens.recap.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.auth.FirebaseAuth
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
    val currentUser = FirebaseAuth.getInstance().currentUser
    private fun getPeriodTime(period: RecapPeriod): Long {
        val calendar = Calendar.getInstance()

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

        return startTime
    }
    fun getMissList(userId: String, period: RecapPeriod, onResult: (List<String>) -> Unit) {
        val startTime = getPeriodTime(period)
        db.collection("misses")
            .whereEqualTo("idUser1", userId)
            .whereGreaterThanOrEqualTo("timestamp", startTime)
            .get()
            .addOnSuccessListener { result ->
                val missedIds = result.documents
                    .mapNotNull { it.getString("idUser2") }

                onResult(missedIds)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
    fun getLikeList(userId: String, period: RecapPeriod, onResult: (List<String>) -> Unit) {
        val startTime = getPeriodTime(period)
        db.collection("likes")
            .whereEqualTo("idUser1", userId)
            .whereGreaterThanOrEqualTo("timestamp", startTime)
            .get()
            .addOnSuccessListener { result ->
                val missedIds = result.documents
                    .mapNotNull { it.getString("idUser2") }

                onResult(missedIds)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
    fun getMatchList(userId: String, period: RecapPeriod, onResult: (List<String>) -> Unit) {
        val startTime = getPeriodTime(period)
        db.collection("matches")
            .whereIn("idUser1", listOf(userId))
            .whereGreaterThanOrEqualTo("timestamp", startTime)
            .get()
            .addOnSuccessListener { result1 ->
                db.collection("matches")
                    .whereIn("idUser2", listOf(userId))
                    .get()
                    .addOnSuccessListener { result2 ->
                        val matchedIds = (result1.documents + result2.documents)
                            .mapNotNull {
                                val id1 = it.getString("idUser1")
                                val id2 = it.getString("idUser2")
                                if (id1 == userId) id2 else id1
                            }
                        onResult(matchedIds.distinct()) // Trả về danh sách không trùng lặp
                    }
                    .addOnFailureListener { onResult(emptyList()) }
            }
            .addOnFailureListener { onResult(emptyList()) }
    }
    fun getMessageSent(userId: String, period: RecapPeriod, onResult: Int) {

    }
    fun getFavoriteTopics(
        likedUsers: List<String>,
        matchedUsers: List<String>,
        onResult: (Map<String, Int>) -> Unit
    ) {
        // Combine and remove duplicates
        val targetUsers = (likedUsers + matchedUsers).distinct()

        if (targetUsers.isEmpty()) {
            onResult(emptyMap())
            return
        }

        // Map to store interest frequencies
        val interestFrequency = mutableMapOf<String, Int>()
        var processedCount = 0

        // For each user, get their interests
        targetUsers.forEach { targetUserId ->
            db.collection("users").document(targetUserId).get()
                .addOnSuccessListener { document ->
                    // Extract interests array
                    val interests = document.get("interests") as? List<String> ?: emptyList()

                    // Count each interest
                    interests.forEach { interest ->
                        interestFrequency[interest] = interestFrequency.getOrDefault(interest, 0) + 1
                    }

                    // Count processed users
                    processedCount++

                    // When all users are processed, return top 5 interests
                    if (processedCount == targetUsers.size) {
                        val topInterests = interestFrequency.entries
                            .sortedByDescending { it.value }
                            .take(5)
                            .associate { it.key to it.value }

                        onResult(topInterests)
                    }
                }
                .addOnFailureListener {
                    // Count as processed even if failed
                    processedCount++

                    // Check if all users are processed
                    if (processedCount == targetUsers.size) {
                        val topInterests = interestFrequency.entries
                            .sortedByDescending { it.value }
                            .take(5)
                            .associate { it.key to it.value }

                        onResult(topInterests)
                    }
                }
        }
    }
    // Function to get total swipes and matches for a specific period
    fun getRecapData(period: RecapPeriod, onResult: (RecapData) -> Unit) {
        val startTime = getPeriodTime(period)

        getMatchList(currentUser?.uid.toString(), period) { matches ->
            getMissList(currentUser?.uid.toString(), period) { misses ->
                getLikeList(currentUser?.uid.toString(), period) { likes ->
                    getFavoriteTopics(likes, matches) { topInterests ->
                        val recapData = RecapData(
                            totalSwipes = matches.size + likes.size + misses.size,
                            rightSwipes = matches.size + likes.size,
                            leftSwipes = misses.size,
                            matches = matches.size,
                            matchRate = if (matches.size + likes.size > 0) {
                                matches.size / (matches.size + likes.size).toFloat()
                            } else 0f,
                            messagesSent = 0,
                            messagesPerMatch = 0f,
                            topConversations = emptyList(),
                            averageActivityMinutes = 0,
                            commonInterests = topInterests
                        )
                        onResult(recapData)
                    }
                }
            }
        }
    }

}