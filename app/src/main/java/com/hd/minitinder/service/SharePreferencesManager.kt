import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec

object SharedPreferencesManager {
    private const val PREFS_NAME = "secure_prefs"

    private fun getEncryptedPreferences(context: Context) =
        EncryptedSharedPreferences.create(
            PREFS_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    // Lưu Private Key dưới dạng Base64 vào EncryptedSharedPreferences
    fun savePrivateKey(context: Context, userId: String, privateKey: ByteArray) {
        val sharedPreferences = getEncryptedPreferences(context)
        sharedPreferences.edit()
            .putString("private_key_$userId", Base64.encodeToString(privateKey, Base64.DEFAULT))
            .apply()
    }

    // Lấy Private Key từ EncryptedSharedPreferences
    fun getPrivateKey(context: Context, userId: String): PrivateKey? {
        val sharedPreferences = getEncryptedPreferences(context)
        val privateKeyString = sharedPreferences.getString("private_key_$userId", null) ?: return null

        val keyBytes = Base64.decode(privateKeyString, Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec)
    }

    // Xóa Private Key khi người dùng đăng xuất
    fun deletePrivateKey(context: Context, userId: String) {
        val sharedPreferences = getEncryptedPreferences(context)
        sharedPreferences.edit()
            .remove("private_key_$userId")
            .apply()
    }
}
