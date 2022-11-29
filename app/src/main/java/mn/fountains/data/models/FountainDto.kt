package mn.fountains.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FountainDto(
    val id: String,
    val name: String,
    val location: LocationDto,
    val properties: FountainPropertiesDto,
)
