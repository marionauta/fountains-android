package mn.openlocations.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mn.openlocations.domain.models.AmenitiesResponse
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.AmenityType
import mn.openlocations.domain.models.FilterSettings
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.repositories.AmenityRepository
import kotlin.native.ObjCName

object GetAmenitiesUseCase {
    private val languages: List<String> = GetLanguagesUseCase()
    private val amenityRepository: AmenityRepository = AmenityRepository

    @ObjCName("callAsFunction")
    operator fun invoke(
        northEast: Location,
        southWest: Location,
        filterSettings: FilterSettings,
    ): Flow<AmenitiesResponse> {
        val includedAmenities = filterSettings.amenities
        return amenityRepository.inside(
            northEast = northEast,
            southWest = southWest,
            languages = languages,
            filters = filterSettings,
        ).map { response ->
            return@map response.filter { amenity ->
                when (amenity) {
                    is Amenity.Fountain -> includedAmenities.contains(AmenityType.DrinkingFountain)
                    is Amenity.Restroom -> includedAmenities.contains(AmenityType.Restroom)
                }
            }
        }
    }
}
