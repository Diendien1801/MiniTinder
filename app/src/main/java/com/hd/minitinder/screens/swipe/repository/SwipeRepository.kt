package com.hd.minitinder.screens.swipe.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hd.minitinder.data.model.UserModel
import com.google.firebase.Timestamp
import com.hd.minitinder.screens.swipe.viewmodel.UserProfile
import java.text.SimpleDateFormat
import java.util.*

class SwipeListRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getFormattedTimestamp(): String {
        val timestamp = Timestamp.now()
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(date)
    }

    fun getUserList(userId: String, onResult: (List<String>) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val userIds = result.documents
                    .map { it.id }
                    .filter { it != userId }
                onResult(userIds)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getMissList(userId: String, onResult: (List<String>) -> Unit) {
        db.collection("misses")
            .whereEqualTo("idUser1", userId)
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

    fun getLikeList(userId: String, onResult: (List<String>) -> Unit) {
        db.collection("likes")
            .whereEqualTo("idUser1", userId)
            .get()
            .addOnSuccessListener { result ->
                val likedIds = result.documents
                    .mapNotNull { it.getString("idUser2") }
                onResult(likedIds)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getMatchList(userId: String, onResult: (List<String>) -> Unit) {
        db.collection("matches")
            .whereIn("idUser1", listOf(userId))
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
                        onResult(matchedIds.distinct())
                    }
                    .addOnFailureListener { onResult(emptyList()) }
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun getAvailableUsers(currentUserId: String, onResult: (List<UserProfile>) -> Unit) {
        // First get all the ids the user has interacted with (likes, misses, matches)
        getLikeList(currentUserId) { likedIds ->
            getMissList(currentUserId) { missedIds ->
                getMatchList(currentUserId) { matchedIds ->
                    // Combine all interacted ids
                    val interactedIds = (likedIds + missedIds + matchedIds).distinct()

                    // Get all users
                    db.collection("users")
                        .get()
                        .addOnSuccessListener { result ->
                            val availableUsers = mutableListOf<UserProfile>()

                            for (document in result.documents) {
                                val userId = document.id

                                // Skip if it's the current user or if the user has interacted with them
                                if (userId == currentUserId || userId in interactedIds) {
                                    continue
                                }

                                // Extract user data
                                val id = document.getString("id") ?: ""
                                val name = document.getString("name") ?: ""
                                val bio = document.getString("bio") ?: ""
                                val dob = document.getString("dob") ?: ""
                                val occupation = document.getString("job") ?: ""
                                val hometown = document.getString("hometown") ?: ""

                                // Calculate age from dob
                                val age = calculateAge(dob)

                                // Get image URLs
                                @Suppress("UNCHECKED_CAST")
                                val imageUrls = document.get("imageUrls") as? List<String> ?: listOf()

                                // Get interests
                                @Suppress("UNCHECKED_CAST")
                                val interests = document.get("interests") as? List<String> ?: listOf()

                                // Create UserProfile object
                                val userProfile = UserProfile(
                                    id = id,
                                    name = name,
                                    age = age,
                                    imageUrls = imageUrls,
                                    tags = interests,
                                    address = hometown,
                                    occupation = occupation,
                                    bio = bio
                                )

                                availableUsers.add(userProfile)
                            }

                            onResult(availableUsers)
                        }
                        .addOnFailureListener {
                            onResult(emptyList())
                        }
                }
            }
        }
    }

    // Helper function to calculate age from date of birth string
    private fun calculateAge(dobString: String): Int {
        try {
            val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val dob = dateFormat.parse(dobString) ?: return 0

            val dobCalendar = Calendar.getInstance().apply { time = dob }
            val currentCalendar = Calendar.getInstance()

            var age = currentCalendar.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)

            // Adjust age if birthday hasn't occurred yet this year
            if (currentCalendar.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--
            }

            return age
        } catch (e: Exception) {
            return 0
        }
    }

    fun miss(userId1: String, userId2: String, onComplete: (Boolean) -> Unit) {
        val missData = hashMapOf(
            "idUser1" to userId1,
            "idUser2" to userId2,
            "timestamp" to  Timestamp.now(),
        )

        db.collection("misses")
            .add(missData)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun like(userId: String, userId2: String, onComplete: (Boolean) -> Unit) {
        val likesRef = db.collection("likes")
        val matchesRef = db.collection("matches")

        likesRef.whereEqualTo("idUser1", userId2)
            .whereEqualTo("idUser2", userId)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val docId = result.documents[0].id
                    likesRef.document(docId).delete()
                        .addOnSuccessListener {
                            val matchData = hashMapOf(
                                "idUser1" to userId,
                                "idUser2" to userId2,
                                "timestamp" to  Timestamp.now(),
                            )
                            matchesRef.add(matchData)
                                .addOnSuccessListener { onComplete(true) }
                                .addOnFailureListener { onComplete(false) }
                        }
                        .addOnFailureListener { onComplete(false) }
                } else {
                    val likeData = hashMapOf(
                        "idUser1" to userId,
                        "idUser2" to userId2,
                        "timestamp" to  Timestamp.now(),
                    )
                    likesRef.add(likeData)
                        .addOnSuccessListener { onComplete(true) }
                        .addOnFailureListener { onComplete(false) }
                }
            }
            .addOnFailureListener { onComplete(false) }
    }

    fun getGoBackList(userId: String, onResult: (List<String>) -> Unit) {
        db.collection("goback")
            .whereEqualTo("idUser1", userId)
            .get()
            .addOnSuccessListener { result ->
                val goBackIds = result.documents
                    .sortedByDescending { it.id }
                    .mapNotNull { it.getString("idUser2") }
                onResult(goBackIds)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun deleteMiss(userId1: String, userId2: String, onComplete: (Boolean) -> Unit) {
        db.collection("misses")
            .whereEqualTo("idUser1", userId1)
            .whereEqualTo("idUser2", userId2)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    for (document in result.documents) {
                        db.collection("misses").document(document.id)
                            .delete()
                            .addOnSuccessListener { onComplete(true) }
                            .addOnFailureListener { onComplete(false) }
                    }
                } else {
                    onComplete(false)
                }
            }
            .addOnFailureListener { onComplete(false) }
    }
}