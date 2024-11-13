package mn.openlocations.domain.usecases

import mn.openlocations.domain.models.PortableUrl
import mn.openlocations.domain.repositories.MapillaryRepository

class GetMapillaryUrlUseCase(mapillaryToken: String) {
    private val repository = MapillaryRepository(mapillaryToken = mapillaryToken)

    suspend operator fun invoke(mapillaryId: String): PortableUrl? {
        return callAsFunction(mapillaryId = mapillaryId)
    }

    suspend fun callAsFunction(mapillaryId: String): PortableUrl? {
        return repository.getImageUrl(mapillaryId = mapillaryId)
    }
}
