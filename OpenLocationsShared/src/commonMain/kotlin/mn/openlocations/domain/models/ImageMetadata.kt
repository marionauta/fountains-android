package mn.openlocations.domain.models

import mn.openlocations.data.models.MapillaryResponseDto
import mn.openlocations.data.models.PanoramaxResponseDto

data class ImageMetadata(
    val imageUrl: Url,
    val creatorUsername: String?,
    val licenseName: String?,
    val licenseUrl: String?,
)

internal fun MapillaryResponseDto.intoDomain(): ImageMetadata? {
    val imageUrl = thumb_1024_url.toPortableUrl() ?: return null
    return ImageMetadata(
        imageUrl = imageUrl,
        creatorUsername = creator?.username,
        licenseName = "CC-BY-SA",
        licenseUrl = "https://creativecommons.org/licenses/by-sa/4.0/",
    )
}

internal fun PanoramaxResponseDto.intoDomain(): ImageMetadata? {
    val feature = features.firstOrNull() ?: return null
    val imageUrl = feature.assets.thumb.href.toPortableUrl() ?: return null
    return ImageMetadata(
        imageUrl = imageUrl,
        creatorUsername = feature.providers.firstOrNull { it.roles.contains("producer") }?.name,
        licenseName = feature.properties.license,
        licenseUrl = feature.links.firstOrNull { it.rel == "license" }?.href,
    )
}
