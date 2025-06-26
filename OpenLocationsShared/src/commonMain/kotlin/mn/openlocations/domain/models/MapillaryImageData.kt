package mn.openlocations.domain.models

import mn.openlocations.data.models.MapillaryResponseDto

data class MapillaryImageData(
    val imageUrl: PortableUrl,
    val creatorUsername: String?,
)

internal fun MapillaryResponseDto.intoDomain(): MapillaryImageData? {
    val imageUrl = thumb_1024_url.toPortableUrl() ?: return null
    return MapillaryImageData(
        imageUrl = imageUrl,
        creatorUsername = creator?.username,
    )
}
