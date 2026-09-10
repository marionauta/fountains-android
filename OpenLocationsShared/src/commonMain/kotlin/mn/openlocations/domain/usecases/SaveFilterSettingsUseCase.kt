package mn.openlocations.domain.usecases

import mn.openlocations.domain.models.FilterSettings
import mn.openlocations.domain.repositories.FilterSettingsRepository
import mn.openlocations.domain.repositories.FilterSettingsRepositoryImpl
import kotlin.native.ObjCName

object SaveFilterSettingsUseCase {
    private val settingsRepository: FilterSettingsRepository = FilterSettingsRepositoryImpl

    @ObjCName("callAsFunction")
    operator fun invoke(settings: FilterSettings) {
        return settingsRepository.saveFilterSettings(settings)
    }
}
