package mn.openlocations.domain.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import mn.openlocations.domain.models.FilterSettings

internal interface FilterSettingsRepository {
    fun getFilterSettings(): Flow<FilterSettings>
    fun saveFilterSettings(settings: FilterSettings)
}

internal object FilterSettingsRepositoryImpl : FilterSettingsRepository {
    private var settings = MutableStateFlow(FilterSettings.default)

    override fun getFilterSettings(): Flow<FilterSettings> {
        return settings
    }

    override fun saveFilterSettings(settings: FilterSettings) {
        this.settings.value = settings
    }
}
