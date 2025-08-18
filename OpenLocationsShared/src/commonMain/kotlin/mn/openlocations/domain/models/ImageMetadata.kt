package mn.openlocations.domain.models

import mn.openlocations.data.models.MapillaryResponseDto
import mn.openlocations.data.models.PanoramaxResponseDto

data class ImageMetadata(
    val imageUrl: Url,
    val creator: Creator?,
    val license: License?,
) {
    data class Creator(val username: String)
    data class License(val name: String, val url: Url)
}

internal fun MapillaryResponseDto.intoDomain(): ImageMetadata? {
    val imageUrl = thumb_1024_url.toPortableUrl() ?: return null
    return ImageMetadata(
        imageUrl = imageUrl,
        creator = creator?.username?.let { ImageMetadata.Creator(it) },
        license = ImageMetadata.License(
            name = "CC-BY-SA",
            url = "https://creativecommons.org/licenses/by-sa/4.0/".toPortableUrl()!!,
        ),
    )
}

internal fun PanoramaxResponseDto.intoDomain(): ImageMetadata? {
    val feature = features.firstOrNull() ?: return null
    val imageUrl = feature.assets.thumb.href.toPortableUrl() ?: return null
    return ImageMetadata(
        imageUrl = imageUrl,
        creator = feature.providers.firstOrNull { it.roles.contains("producer") }?.name?.let {
            ImageMetadata.Creator(it)
        },
        license = feature.links.firstOrNull { it.rel == "license" }?.href?.toPortableUrl()?.let {
            ImageMetadata.License(
                name = feature.properties.license,
                url = it,
            )
        },
    )
}
