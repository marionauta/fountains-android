package mn.openlocations.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mn.openlocations.domain.models.AmenitiesResponse
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.AmenityType
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.repositories.AmenityRepository
import mn.openlocations.domain.repositories.FilterSettingsRepository
import mn.openlocations.domain.repositories.FilterSettingsRepositoryImpl
import kotlin.native.ObjCName

class GetAmenitiesUseCase(
    private val languages: List<String> = GetLanguagesUseCase(),
    private val amenityRepository: AmenityRepository = AmenityRepository,
    private val settingsRepository: FilterSettingsRepository = FilterSettingsRepositoryImpl(),
) {
    @ObjCName("callAsFunction")
    suspend operator fun invoke(northEast: Location, southWest: Location): Flow<AmenitiesResponse> {
        val settings = settingsRepository.getFilterSettings()
        val response = amenityRepository.inside(
            northEast = northEast,
            southWest = southWest,
            languages = languages,
        )
        return response.map { response ->
            return@map response.filter { amenity ->
                when (amenity) {
                    is Amenity.Fountain -> settings.amenities.contains(AmenityType.DrinkingFountain)
                    is Amenity.Restroom -> settings.amenities.contains(AmenityType.Restroom)
                }
            }
        }
    }
}
