package mn.openlocations.domain.repositories

import android.content.Context
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mn.openlocations.BuildConfig

class PreferencesRepository(context: Context) {
    companion object {
        private const val preferencesKey = "${BuildConfig.APPLICATION_ID}.PREFERENCES"
        private const val adsKey = "${BuildConfig.APPLICATION_ID}.ADS.${BuildConfig.VERSION_CODE}"
        private const val mapClusterKey = "${BuildConfig.APPLICATION_ID}.CONFIG.CLUSTER"
    }

    private val preferences = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE)
    private val flowing = FlowSharedPreferences(preferences)

    fun getShowAds(): Flow<Boolean> {
        if (BuildConfig.DEBUG) {
            return flow { emit(false) }
        }
        return flowing.getBoolean(adsKey, true).asFlow()
    }

    fun toggleShowAds() {
        val current = preferences.getBoolean(adsKey, true)
        with(preferences.edit()) {
            putBoolean(adsKey, !current)
            apply()
        }
    }

    fun getMapClusteringEnabled(): Flow<Boolean> {
        return flowing.getBoolean(mapClusterKey, false).asFlow()
    }

    fun toggleMapClustering() {
        val current = preferences.getBoolean(mapClusterKey, false)
        with(preferences.edit()) {
            putBoolean(mapClusterKey, !current)
            apply()
        }
    }
}
