package mn.openlocations.domain.repositories

import mn.openlocations.domain.models.FilterSettings

internal interface FilterSettingsRepository {
    fun getFilterSettings(): FilterSettings
    fun saveFilterSettings(settings: FilterSettings)
}

internal object FilterSettingsRepositoryImpl : FilterSettingsRepository {
    private var settings: FilterSettings = FilterSettings.default

    override fun getFilterSettings(): FilterSettings {
        return settings
    }

    override fun saveFilterSettings(settings: FilterSettings) {
        this.settings = settings
    }
}
