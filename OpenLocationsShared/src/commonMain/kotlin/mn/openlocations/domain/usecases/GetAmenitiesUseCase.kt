package mn.openlocations.domain.usecases

import mn.openlocations.domain.models.AmenitiesResponse
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.AmenityType
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.repositories.AmenityRepository
import mn.openlocations.domain.repositories.FilterSettingsRepository
import mn.openlocations.domain.repositories.FilterSettingsRepositoryImpl
import kotlin.native.ObjCName

class GetAmenitiesUseCase(
    private val amenityRepository: AmenityRepository = AmenityRepository(),
    private val settingsRepository: FilterSettingsRepository = FilterSettingsRepositoryImpl(),
) {

    @ObjCName("callAsFunction")
    suspend operator fun invoke(northEast: Location, southWest: Location): AmenitiesResponse? {
        val settings = settingsRepository.getFilterSettings()
        val response = amenityRepository.inside(northEast = northEast, southWest = southWest)
        return response?.let {
            AmenitiesResponse(
                lastUpdated = it.lastUpdated,
                amenities = it.amenities.filter { amenity ->
                    when (amenity) {
                        is Amenity.Fountain -> settings.amenties.contains(AmenityType.DrinkingFountain)
                        is Amenity.Restroom -> settings.amenties.contains(AmenityType.Restroom)
                    }
                }
            )
        }
    }
}
