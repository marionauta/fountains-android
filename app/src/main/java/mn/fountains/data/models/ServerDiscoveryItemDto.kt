package mn.fountains.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ServerDiscoveryItemDto(
    val name: String,
    val address: String,
)
