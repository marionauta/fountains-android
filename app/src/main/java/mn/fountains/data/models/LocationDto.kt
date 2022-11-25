package mn.fountains.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val latitude: Double,
    val longitude: Double,
)