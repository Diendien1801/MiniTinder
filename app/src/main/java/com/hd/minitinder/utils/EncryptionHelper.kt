package com.hd.minitinder.utils

import android.util.Base64
import android.util.Log
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher

object EncryptionHelper {

    fun encryptMessage(plainText: String, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decryptMessage(encryptedText: String, privateKey: PrivateKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedBytes = cipher.doFinal(Base64.decode(encryptedText, Base64.DEFAULT))
        return String(decryptedBytes)
    }
    fun base64ToPrivateKey(privateKeyBase64: String): PrivateKey? {
        return try {
            val keyBytes = Base64.decode(privateKeyBase64, Base64.DEFAULT)
            val keySpec = PKCS8EncodedKeySpec(keyBytes)
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec)


        } catch (e: Exception) {
            Log.e("KeyConversion", "Failed to convert base64 to PrivateKey", e)
            null
        }
    }

}