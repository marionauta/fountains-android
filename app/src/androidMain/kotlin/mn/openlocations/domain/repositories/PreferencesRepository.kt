package mn.openlocations.domain.repositories

import android.content.Context
import androidx.core.content.edit
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mn.openlocations.BuildConfig

class PreferencesRepository(context: Context) {
    companion object {
        internal const val DEFAULT_MAX_DISTANCE = 15_000f

        private const val PREFERENCES_KEY = "${BuildConfig.APPLICATION_ID}.PREFERENCES"
        private const val ADS_KEY = "${BuildConfig.APPLICATION_ID}.ADS.${BuildConfig.VERSION_CODE}"
        private const val MAP_MAX_DISTANCE_KEY =
            "${BuildConfig.APPLICATION_ID}.CONFIG.MAP_MAX_DISTANCE"
    }

    private val preferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
    private val flowing = FlowSharedPreferences(preferences)

    fun getShowAds(): Flow<Boolean> {
        if (BuildConfig.DEBUG) {
            return flow { emit(false) }
        }
        return flowing.getBoolean(ADS_KEY, true).asFlow()
    }

    fun toggleShowAds() {
        val current = preferences.getBoolean(ADS_KEY, true)
        preferences.edit {
            putBoolean(ADS_KEY, !current)
        }
    }

    fun getMapMaxDistance(): Flow<Float> {
        return flowing.getFloat(MAP_MAX_DISTANCE_KEY, DEFAULT_MAX_DISTANCE).asFlow()
    }

    fun setMapMaxDistance(distance: Float) {
        preferences.edit {
            putFloat(MAP_MAX_DISTANCE_KEY, distance)
        }
    }
}
