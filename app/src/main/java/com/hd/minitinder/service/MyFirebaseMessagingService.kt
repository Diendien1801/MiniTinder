package com.hd.minitinder.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hd.minitinder.MainActivity
import com.hd.minitinder.R
import com.hd.minitinder.navigation.NavigationItem

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: ${remoteMessage.data["senderId"]}")
            showNotification(it.title ?: "MiniTinder", it.body ?: "Bạn có tin nhắn mới!")
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "CHAT_CHANEL_ID"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Tin nhắn mới",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Cập nhật Intent với cờ phù hợp
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", NavigationItem.Chat.route)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.tinder_gold)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())
    }

    fun saveUserFCMToken(userId: String, token: String) {
        val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

        val data = hashMapOf("fcmToken" to token)

        userRef.set(data, SetOptions.merge())
            .addOnSuccessListener { Log.d("FCM", "Token đã được cập nhật vào Firestore!") }
            .addOnFailureListener { e -> Log.e("FCM", "Lưu token thất bại", e) }
    }
}
