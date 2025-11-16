package mn.openlocations.domain.usecases

import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.repositories.AmenityRepository
import kotlin.native.ObjCName

class GetAmenityUseCase(
    private val languages: List<String> = GetLanguagesUseCase(),
    private val amenityRepository: AmenityRepository = AmenityRepository,
) {
    @ObjCName("callAsFunction")
    operator fun invoke(amenityId: String): Amenity? {
        return amenityRepository.get(amenityId = amenityId, languages = languages)
    }
}
