package mn.openlocations.domain.repositories

import android.content.Context
import mn.openlocations.BuildConfig

class PreferencesRepository(private val context: Context) {
    companion object {
        private const val preferencesKey = "${BuildConfig.APPLICATION_ID}.PREFERENCES"
        private const val adsKey = "${BuildConfig.APPLICATION_ID}.ADS.${BuildConfig.VERSION_CODE}"
    }

    fun getShowAds(): Boolean {
        if (BuildConfig.DEBUG) {
            return false
        }
        val prefs = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE)
        return prefs?.getBoolean(adsKey, true) ?: true
    }

    fun toggleShowAds() {
        val prefs = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE) ?: return
        val current = prefs.getBoolean(adsKey, true)
        with(prefs.edit()) {
            putBoolean(adsKey, !current)
            apply()
        }
    }
}
