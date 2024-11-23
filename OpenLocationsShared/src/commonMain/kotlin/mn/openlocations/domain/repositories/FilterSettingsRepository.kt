package mn.openlocations.domain.repositories

import mn.openlocations.domain.models.AmenityType
import mn.openlocations.domain.models.FilterSettings

interface FilterSettingsRepository {
    fun getFilterSettings(): FilterSettings
}

class FilterSettingsRepositoryImpl: FilterSettingsRepository {
    override fun getFilterSettings(): FilterSettings {
        return FilterSettings(amenties = setOf(AmenityType.DrinkingFountain, AmenityType.Restroom))
    }
}
