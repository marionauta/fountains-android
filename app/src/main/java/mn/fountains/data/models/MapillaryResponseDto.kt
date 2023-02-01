package mn.fountains.data.models

@kotlinx.serialization.Serializable
data class MapillaryResponseDto(
    val id: String,
    val thumb_1024_url: String,
)
