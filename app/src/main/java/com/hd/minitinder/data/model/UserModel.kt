package com.hd.minitinder.data.model


import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

data class UserModel(
    val id: String = "",
    val imageUrls: List<String> = listOf(),
    val name: String = "",
    val gender: String = "",
    val dob: String = "",
    val hometown: String = "",
    val job: String = "",
    val interests: List<String> = listOf(),
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val phoneNumber: String = "",
    val bio: String = "",
    val isPremium: Boolean = false
) {
    // Chuyển đối tượng UserModel thành Map để lưu lên Firestore
    fun toJson(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "imageUrls" to imageUrls,
            "name" to name,
            "gender" to gender,
            "dob" to dob,
            "hometown" to hometown,
            "job" to job,
            "interests" to interests,
            "height" to height,
            "weight" to weight,
            "phoneNumber" to phoneNumber,
            "bio" to bio,
            "isPremium" to isPremium
        )
    }

    companion object {
        // Chuyển Map từ Firestore thành UserModel
        fun fromJson(snapshot: DocumentSnapshot): UserModel {
            return UserModel(
                id = snapshot.getString("id") ?: "",
                imageUrls = snapshot.get("imageUrls") as? List<String> ?: listOf(),
                name = snapshot.getString("name") ?: "",
                gender = snapshot.getString("gender") ?: "",
                dob = snapshot.getString("dob") ?: "",
                hometown = snapshot.getString("hometown") ?: "",
                job = snapshot.getString("job") ?: "",
                interests = snapshot.get("interests") as? List<String> ?: listOf(),
                height = snapshot.getDouble("height") ?: 0.0,
                weight = snapshot.getDouble("weight") ?: 0.0,
                phoneNumber = snapshot.getString("phoneNumber") ?: "",
                bio = snapshot.getString("bio") ?: "",
                isPremium = snapshot.getBoolean("isPremium") ?: false
            )
        }
    }
}
