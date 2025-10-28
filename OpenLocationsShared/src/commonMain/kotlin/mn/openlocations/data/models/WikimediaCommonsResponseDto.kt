package mn.openlocations.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class WikimediaCommonsResponseDto(val query: Query)

@Serializable
internal class Query(val pages: Map<String, Page>)

@Serializable
internal class Page(@SerialName("imageinfo") val imageInfo: List<ImageInfo>)

@Serializable
internal class ImageInfo(
    @SerialName("thumburl") val thumbUrl: String,
    @SerialName("extmetadata") val metadata: ExtMetadata,
)

@Serializable
internal class ExtMetadata(
    val Artist: ExtMetadataValue,
    val LicenseShortName: ExtMetadataValue,
    val LicenseUrl: ExtMetadataValue,
)

@Serializable
internal class ExtMetadataValue(val value: String)
