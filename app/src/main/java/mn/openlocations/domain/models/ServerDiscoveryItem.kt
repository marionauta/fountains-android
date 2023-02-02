package mn.openlocations.domain.models

import mn.openlocations.data.models.ServerDiscoveryItemDto
import java.net.URL

data class ServerDiscoveryItem(
    val name: String,
    val address: URL,
)

fun ServerDiscoveryItemDto.intoDomain(): ServerDiscoveryItem = ServerDiscoveryItem(
    name = name,
    address = URL(address),
)
