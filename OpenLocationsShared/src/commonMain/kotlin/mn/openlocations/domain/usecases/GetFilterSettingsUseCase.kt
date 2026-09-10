package mn.openlocations.domain.usecases

import mn.openlocations.domain.models.FilterSettings
import mn.openlocations.domain.repositories.FilterSettingsRepository
import mn.openlocations.domain.repositories.FilterSettingsRepositoryImpl
import kotlin.native.ObjCName

object GetFilterSettingsUseCase {
    private val settingsRepository: FilterSettingsRepository = FilterSettingsRepositoryImpl

    @ObjCName("callAsFunction")
    internal operator fun invoke(): FilterSettings {
        return settingsRepository.getFilterSettings()
    }
}
