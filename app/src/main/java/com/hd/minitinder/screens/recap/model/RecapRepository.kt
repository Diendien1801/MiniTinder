package com.hd.minitinder.screens.recap.model

import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import com.google.firebase.Timestamp

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
    private fun getPeriodTime(period: RecapPeriod): Timestamp {
        val calendar = Calendar.getInstance()

        when (period) {
            RecapPeriod.Daily -> calendar.add(Calendar.DAY_OF_YEAR, -1)
            RecapPeriod.Weekly -> calendar.add(Calendar.WEEK_OF_YEAR, -1)
            RecapPeriod.Monthly -> calendar.add(Calendar.MONTH, -1)
        }

        return Timestamp(calendar.timeInMillis / 1000, 0)
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
            .addOnFailureListener { e ->
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
            .whereGreaterThanOrEqualTo("timestamp", startTime)
            .whereIn("idUser1", listOf(userId))
            .whereIn("idUser2", listOf(userId))
            .get()
            .addOnSuccessListener { result ->
                // Combine both "idUser1" and "idUser2" values into a single list of matched users
                val matchedIds = result.documents.mapNotNull {
                    val id1 = it.getString("idUser1")
                    val id2 = it.getString("idUser2")
                    when {
                        id1 == userId -> id2
                        id2 == userId -> id1
                        else -> null
                    }
                }

                // Return a distinct list of matched IDs (no duplicates)
                onResult(matchedIds.distinct())
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun getMessageCount(userId: String, startTime: Timestamp, onResult: (Int) -> Unit) {
        db.collection("chats")
            .get()
            .addOnSuccessListener { chatDocs ->
                if (chatDocs.isEmpty) {
                    onResult(0)
                    return@addOnSuccessListener
                }

                var completedChats = 0
                var totalMessageCount = 0


                for (chatDoc in chatDocs.documents) {
                    val chatId = chatDoc.id

                    // Truy vấn tất cả tin nhắn trong subcollection "messages" của chat này
                    chatDoc.reference.collection("messages")
                        .get()
                        .addOnSuccessListener { messageSnapshot ->

                            for (messageDoc in messageSnapshot.documents) {
                                val senderId = messageDoc.getString("senderId")
                                val receiverId = messageDoc.getString("receiverId")
                                val timestamp = messageDoc.getLong("timestamp") ?: 0L

                                // Kiểm tra xem tin nhắn có liên quan đến người dùng và có trong khoảng thời gian hay không
                                if ((senderId == userId || receiverId == userId) &&
                                    timestamp / 1000 >= startTime.seconds) {
                                    totalMessageCount++
                                }
                            }

                            // Đánh dấu là đã hoàn thành xử lý chat này
                            completedChats++

                            // Kiểm tra xem đã xử lý hết tất cả chat chưa
                            if (completedChats == chatDocs.size()) {
                                onResult(totalMessageCount)
                            }
                        }
                        .addOnFailureListener { e ->

                            // Ngay cả khi lỗi, vẫn đánh dấu chat này đã được xử lý
                            completedChats++

                            if (completedChats == chatDocs.size()) {
                                onResult(totalMessageCount)
                            }
                        }
                }
            }
            .addOnFailureListener { e ->
                onResult(0)
            }
    }
    fun getTop3Users(userId: String, startTime: Timestamp, onResult: (List<TopConversation>) -> Unit) {
        db.collection("chats")
            .get()
            .addOnSuccessListener { chats ->
                if (chats.isEmpty) {
                    Log.d("RecapRepo", "No chats found")
                    onResult(emptyList())
                    return@addOnSuccessListener
                }

                var completedChats = 0
                val messageCountMap = mutableMapOf<String, Int>()
                Log.d("RecapRepo", "Found ${chats.size()} chat documents")

                for (chat in chats.documents) {
                    val chatId = chat.id
                    Log.d("RecapRepo", "Processing chat ID: $chatId for top users")

                    chat.reference.collection("messages")
                        .get()
                        .addOnSuccessListener { messages ->
                            Log.d("RecapRepo", "Found ${messages.size()} messages in chat $chatId")

                            for (message in messages.documents) {
                                val senderId = message.getString("senderId") ?: ""
                                val receiverId = message.getString("receiverId") ?: ""
                                val timestamp = message.getLong("timestamp") ?: 0L

                                if (timestamp / 1000 >= startTime.seconds) {
                                    when {
                                        senderId == userId && receiverId.isNotEmpty() -> {
                                            messageCountMap[receiverId] = messageCountMap.getOrDefault(receiverId, 0) + 1
                                            Log.d("RecapRepo", "Counted message from $userId to $receiverId, total: ${messageCountMap[receiverId]}")
                                        }
                                        receiverId == userId && senderId.isNotEmpty() -> {
                                            messageCountMap[senderId] = messageCountMap.getOrDefault(senderId, 0) + 1
                                            Log.d("RecapRepo", "Counted message from $senderId to $userId, total: ${messageCountMap[senderId]}")
                                        }
                                    }
                                }
                            }

                            completedChats++
                            Log.d("RecapRepo", "Completed processing $completedChats of ${chats.size()} chats for top users")

                            if (completedChats == chats.size()) {
                                Log.d("RecapRepo", "All chats processed. Top users count: ${messageCountMap.size}")
                                // Lấy top 3 user có nhiều tin nhắn nhất
                                val topUserIds = messageCountMap.entries
                                    .sortedByDescending { it.value }
                                    .take(3)
                                    .map { it.key to it.value }

                                if (topUserIds.isEmpty()) {
                                    Log.d("RecapRepo", "No top users found")
                                    onResult(emptyList())
                                    return@addOnSuccessListener
                                }

                                // Lấy tên người dùng từ collection users
                                fetchUserNames(topUserIds) { topUsersWithNames ->
                                    Log.d("RecapRepo", "Final top users: $topUsersWithNames")
                                    val topConversations = topUsersWithNames.map { (userName, id) ->
                                        TopConversation(name = userName, messageCount = id)
                                    }
                                    onResult(topConversations)
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("RecapRepo", "Error getting messages for chat $chatId", e)
                            completedChats++

                            if (completedChats == chats.size()) {
                                val topUserIds = messageCountMap.entries
                                    .sortedByDescending { it.value }
                                    .take(3)
                                    .map { it.key to it.value }

                                fetchUserNames(topUserIds) { topUsersWithNames ->
                                    Log.d("RecapRepo", "Final top users: $topUsersWithNames")
                                    val topConversations = topUsersWithNames.map { (userName, id) ->
                                        TopConversation(name = userName, messageCount = id)
                                    }
                                    onResult(topConversations)
                                }
                            }
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("RecapRepo", "Failed to get chats for top users", e)
                onResult(emptyList())
            }
    }

    // Hàm trợ giúp để lấy tên người dùng từ danh sách ID
    private fun fetchUserNames(userIdsWithCount: List<Pair<String, Int>>, onComplete: (List<Pair<String, Int>>) -> Unit) {
        if (userIdsWithCount.isEmpty()) {
            onComplete(emptyList())
            return
        }

        val result = mutableListOf<Pair<String, Int>>()
        var completedQueries = 0

        for ((userId, count) in userIdsWithCount) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { userDoc ->
                    // Lấy tên người dùng từ document, nếu không có thì sử dụng ID
                    val name = userDoc.getString("name") ?: userDoc.getString("displayName") ?: userId

                    Log.d("RecapRepo", "Fetched name for user $userId: $name")
                    result.add(Pair(name, count))

                    completedQueries++
                    if (completedQueries == userIdsWithCount.size) {
                        // Sắp xếp lại kết quả theo số lượng tin nhắn (giảm dần)
                        val sortedResult = result.sortedByDescending { it.second }
                        onComplete(sortedResult)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("RecapRepo", "Error fetching user info for $userId", e)
                    // Nếu không lấy được thông tin, sử dụng ID làm tên
                    result.add(Pair(userId, count))

                    completedQueries++
                    if (completedQueries == userIdsWithCount.size) {
                        val sortedResult = result.sortedByDescending { it.second }
                        onComplete(sortedResult)
                    }
                }
        }
    }
    fun getFavoriteTopics(
        likedUsers: List<String>,
        matchedUsers: List<String>,
        onResult: (Map<String, Int>) -> Unit
    ) {
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
        val id = currentUser?.uid.toString()
        getMatchList(id, period) { matches ->
            getMissList(id, period) { misses ->
                getLikeList(id, period) { likes ->
                    getFavoriteTopics(likes, matches) { topInterests ->
                        getMessageCount(id, startTime) { messageCount ->
                            getTop3Users(id, startTime) { topConversations ->
                                val messagesPerMatch = if (matches.size > 0) {
                                    messageCount.toFloat() / matches.size
                                } else 0f

                                val recapData = RecapData(
                                    totalSwipes = matches.size + likes.size + misses.size,
                                    rightSwipes = matches.size + likes.size,
                                    leftSwipes = misses.size,
                                    matches = matches.size,
                                    matchRate = if (matches.size + likes.size > 0) {
                                        matches.size / (matches.size + likes.size).toFloat()
                                    } else 0f,
                                    messagesSent = messageCount,
                                    messagesPerMatch = messagesPerMatch,
                                    topConversations = topConversations,
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
    }

}