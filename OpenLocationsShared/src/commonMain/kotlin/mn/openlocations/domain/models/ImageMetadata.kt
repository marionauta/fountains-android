package mn.openlocations.domain.models

import mn.openlocations.data.models.MapillaryResponseDto
import mn.openlocations.data.models.PanoramaxResponseDto
import mn.openlocations.data.models.WikimediaCommonsResponseDto

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

internal fun WikimediaCommonsResponseDto.intoDomain(): ImageMetadata? {
    val info = query.pages.values.firstOrNull()?.imageInfo?.firstOrNull() ?: return null
    val thumbUrl = info.thumbUrl.toPortableUrl() ?: return null
    var username = info.metadata.Artist.value
    if (username.contains(">")) {
        username = username.substringAfter('>').substringBefore('<')
    }
    return ImageMetadata(
        imageUrl = thumbUrl,
        creator = ImageMetadata.Creator(username = username),
        license = info.metadata.LicenseUrl.value.toPortableUrl()?.let {
            ImageMetadata.License(
                name = info.metadata.LicenseShortName.value,
                url = it,
            )
        },
    )
}
