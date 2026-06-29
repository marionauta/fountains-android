package mn.openlocations.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MapillaryResponseDto(
    val creator: MapillaryImageCreatorDto?,
    @SerialName("thumb_1024_url")
    val thumbUrl: String,
)

@Serializable
internal data class MapillaryImageCreatorDto(
    val username: String,
)
