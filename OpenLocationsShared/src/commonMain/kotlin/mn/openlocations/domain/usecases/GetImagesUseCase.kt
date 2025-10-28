package mn.openlocations.domain.usecases

import mn.openlocations.domain.models.ImageMetadata
import mn.openlocations.domain.models.ImageSource
import mn.openlocations.domain.models.toPortableUrl
import mn.openlocations.domain.repositories.MapillaryRepository
import mn.openlocations.domain.repositories.PanoramaxRepository
import mn.openlocations.domain.repositories.WikimediaCommonsRepository
import kotlin.native.ObjCName

class GetImagesUseCase(mapillaryToken: String) {
    private val mapillaryRepository = MapillaryRepository(mapillaryToken = mapillaryToken)
    private val panoramaxRepository = PanoramaxRepository

    private var wikimediaRepository = WikimediaCommonsRepository

    @ObjCName("callAsFunction")
    suspend operator fun invoke(images: List<Pair<ImageSource, String>>): List<ImageMetadata> {
        val results = mutableListOf<ImageMetadata>()
        for ((source, id) in images) {
            when (source) {
                ImageSource.Mapillary -> {
                    val result = mapillaryRepository.getImageMetadata(mapillaryId = id) ?: continue
                    results.add(result)
                }

                ImageSource.Panoramax -> {
                    val result = panoramaxRepository.getImageMetadata(panoramaxId = id) ?: continue
                    results.add(result)
                }

                ImageSource.WikimediaCommons -> {
                    val result = wikimediaRepository.getImageMetadata(name = id) ?: continue
                    results.add(result)
                }

                ImageSource.Url -> {
                    val url = id.toPortableUrl() ?: continue
                    val result = ImageMetadata(
                        imageUrl = url,
                        creator = null,
                        license = null,
                    )
                    results.add(result)
                }
            }
        }
        return results
    }
}
