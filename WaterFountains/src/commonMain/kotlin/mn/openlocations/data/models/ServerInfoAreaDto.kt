package mn.openlocations.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ServerInfoAreaDto(
    val displayName: String,
    val location: LocationDto,
)
