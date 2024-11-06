package mn.openlocations.data.models

@kotlinx.serialization.Serializable
data class MapillaryResponseDto(
    val id: String,
    val thumb_1024_url: String,
)
