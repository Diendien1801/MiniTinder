package com.hd.minitinder.screens.detailChat.repositories

import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.privacysandbox.tools.core.model.Method
import com.cloudinary.utils.ObjectUtils
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hd.minitinder.screens.detailChat.model.ChatMessageModel
import com.hd.minitinder.service.CloudinaryManager
import com.hd.minitinder.utils.EncryptionHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlinx.coroutines.withContext


import java.io.InputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.X509EncodedKeySpec
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Callback
import okhttp3.Call
import okhttp3.Response
import java.io.IOException
import java.io.FileInputStream
class ChatMessageRepository {
    fun sendMessage(context: Context, chatId: String, message: ChatMessageModel, senderId: String, receiverId: String) {
        val db = Firebase.firestore

        val senderRef = db.collection("users").document(senderId)
        val receiverRef = db.collection("users").document(receiverId)

        db.runTransaction { transaction ->
            val senderDoc = transaction.get(senderRef)
            val receiverDoc = transaction.get(receiverRef)

            val senderPublicKeyString = senderDoc.getString("publicKey")
            val receiverPublicKeyString = receiverDoc.getString("publicKey")

            if (senderPublicKeyString.isNullOrEmpty() || receiverPublicKeyString.isNullOrEmpty()) {
                throw Exception("Missing public keys!")
            }

            val senderPublicKey = KeyFactory.getInstance("RSA").generatePublic(
                X509EncodedKeySpec(Base64.decode(senderPublicKeyString, Base64.DEFAULT))
            )
            val receiverPublicKey = KeyFactory.getInstance("RSA").generatePublic(
                X509EncodedKeySpec(Base64.decode(receiverPublicKeyString, Base64.DEFAULT))
            )

            // Mã hóa tin nhắn hai lần
            val encryptedForReceiver = EncryptionHelper.encryptMessage(message.message, receiverPublicKey)
            val encryptedForSender = EncryptionHelper.encryptMessage(message.message, senderPublicKey)

            // Tạo bản sao của tin nhắn với nội dung đã mã hóa
            val encryptedMessage = message.copy(
                message = encryptedForReceiver,
                encryptForSender = encryptedForSender
            )

            // Lưu vào Firestore
            transaction.set(
                db.collection("chats").document(chatId).collection("messages").document(),
                encryptedMessage
            )
        }.addOnSuccessListener {
            Log.d("Chat", "Tin nhắn đã được gửi!")
        }.addOnFailureListener { e ->
            Log.e("Chat", "Gửi tin nhắn thất bại", e)
        }
    }



    fun listenForMessages(
        chatId: String,
        userId: String, // Thêm ID người dùng để xác định vai trò
        privateKey: PrivateKey,
        onMessageReceived: (List<ChatMessageModel>) -> Unit
    ) {
        val db = Firebase.firestore
        db.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Chat", "Lỗi khi lắng nghe tin nhắn", error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull { document ->
                    val chatMessage = document.toObject(ChatMessageModel::class.java) ?: return@mapNotNull null

                    // Xác định dữ liệu cần giải mã
                    val encryptedData = if (chatMessage.senderId == userId) {
                        chatMessage.encryptForSender
                    } else {
                        chatMessage.message
                    }

                    // Giải mã tin nhắn
                    val decryptedMessage = try {
                        EncryptionHelper.decryptMessage(encryptedData, privateKey)
                    } catch (e: Exception) {
                        Log.e("Chat", "Decryption failed: ${e.message}")
                        encryptedData // Nếu lỗi, giữ nguyên bản mã hóa
                    }

                    chatMessage.copy(message = decryptedMessage)
                } ?: emptyList()

                onMessageReceived(messages)
            }
    }



    suspend fun uploadImageToCloudinary(context: Context, imageUri: Uri): String? {
            return withContext(Dispatchers.IO) {
                try {
                    val inputStream = context.contentResolver.openInputStream(imageUri)
                    inputStream?.use { stream ->  // Đảm bảo đóng InputStream sau khi dùng
                        val result = CloudinaryManager.cloudinary.uploader().upload(
                            stream,
                            ObjectUtils.emptyMap()
                        )
                        result["secure_url"] as String
                    }
                } catch (e: Exception) {
                    Log.e("Upload", "Lỗi upload: ${e.message}")
                    null
                }
            }
        }


        private fun getRealPathFromURI(context: Context, uri: Uri): String? {
            var filePath: String? = null
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex("_data")
                    filePath = if (columnIndex != -1) cursor.getString(columnIndex) else null
                }
            }
            return filePath
        }

}