package mn.openlocations.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ServerDiscoveryItemDto(
    val q: Boolean?,
    val name: String,
    val address: String,
)
