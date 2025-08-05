package mn.openlocations.domain.usecases

import mn.openlocations.domain.models.Url
import mn.openlocations.domain.repositories.MapillaryRepository
import kotlin.native.ObjCName

class GetMapillaryUrlUseCase(mapillaryToken: String) {
    private val repository = MapillaryRepository(mapillaryToken = mapillaryToken)

    @ObjCName("callAsFunction")
    suspend operator fun invoke(mapillaryId: String): Url? {
        return repository.getImageUrl(mapillaryId = mapillaryId)?.imageUrl
    }
}
