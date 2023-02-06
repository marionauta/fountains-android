package mn.openlocations.domain.models

import mn.openlocations.data.models.ServerDiscoveryItemDto
import java.net.URL

data class ServerDiscoveryItem(
    val reviewed: Boolean,
    val name: String,
    val address: URL,
)

fun ServerDiscoveryItemDto.intoDomain(): ServerDiscoveryItem = ServerDiscoveryItem(
    reviewed = q ?: false,
    name = name,
    address = URL(address),
)
