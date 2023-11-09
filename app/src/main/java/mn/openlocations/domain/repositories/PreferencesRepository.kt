package mn.openlocations.domain.repositories

import android.content.Context
import mn.openlocations.BuildConfig

class PreferencesRepository(context: Context) {
    companion object {
        private const val preferencesKey = "${BuildConfig.APPLICATION_ID}.PREFERENCES"
        private const val adsKey = "${BuildConfig.APPLICATION_ID}.ADS.${BuildConfig.VERSION_CODE}"
        private const val mapClusterKey = "${BuildConfig.APPLICATION_ID}.CONFIG.CLUSTER"
    }

    private val preferences = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE)

    fun getShowAds(): Boolean {
        if (BuildConfig.DEBUG) {
            return false
        }
        return preferences.getBoolean(adsKey, true)
    }

    fun getMapClusteringEnabled(): Boolean {
        return preferences.getBoolean(mapClusterKey, false)
    }

    fun toggleShowAds() {
        val current = preferences.getBoolean(adsKey, true)
        with(preferences.edit()) {
            putBoolean(adsKey, !current)
            apply()
        }
    }
}
