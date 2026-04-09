package mn.openlocations.domain.usecases

import kotlinx.coroutines.flow.Flow
import mn.openlocations.data.models.OsmId
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.repositories.AmenityRepository
import kotlin.native.ObjCName

class GetAmenityUseCase(
    private val languages: List<String> = GetLanguagesUseCase(),
    private val amenityRepository: AmenityRepository = AmenityRepository,
) {
    @ObjCName("callAsFunction")
    operator fun invoke(osmId: OsmId): Flow<Amenity> {
        return amenityRepository.get(osmId = osmId, languages = languages)
    }
}
