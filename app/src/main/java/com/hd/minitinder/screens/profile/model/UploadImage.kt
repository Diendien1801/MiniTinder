package com.hd.minitinder.screens.profile.model

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadImageToFirebase(
    imageUri: Uri,
    onSuccess: (downloadUrl: String) -> Unit,
    onFailure: (exception: Exception) -> Unit
) {
    // Kiểm tra Uri hợp lệ
    if (imageUri == Uri.EMPTY) {
        onFailure(IllegalArgumentException("Invalid image URI"))
        return
    }

    val fileName = "images/${UUID.randomUUID()}.jpg"
    val storageReference = FirebaseStorage.getInstance().reference.child(fileName)

    storageReference.putFile(imageUri)
        .addOnSuccessListener {

            storageReference.downloadUrl
                .addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                    Log.d("FirebaseUpload", "Upload successful: $uri")
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                    Log.e("FirebaseUpload", "Failed to get download URL", e)
                }
        }
        .addOnFailureListener { e ->
            onFailure(e)
            Log.e("FirebaseUpload", "Upload failed", e)
        }
}