package mn.openlocations.domain.repositories

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import mn.openlocations.domain.utils.SecureStringStorage

internal class StringStorageRepository(context: Context) : SecureStringStorage {
    private val key = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val secure = EncryptedSharedPreferences.create(
        "SecureStringStorage",
        key,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    override fun set(key: String, value: String) {
        with(secure.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun get(key: String): String? {
        return secure.getString(key, null)
    }
}
