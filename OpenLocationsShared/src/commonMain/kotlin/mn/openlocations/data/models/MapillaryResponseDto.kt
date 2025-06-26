package mn.openlocations.data.models

import kotlinx.serialization.Serializable

@Serializable
internal data class MapillaryResponseDto(
    val creator: MapillaryImageCreatorDto?,
    val thumb_1024_url: String,
)

@Serializable
internal data class MapillaryImageCreatorDto(
    val username: String,
)
