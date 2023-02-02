package mn.openlocations.domain.models

import mn.openlocations.data.models.ServerEntity
import java.net.URL

data class Server(
    val name: String,
    val address: URL,
    val location: Location,
)

fun ServerEntity.intoDomain(): Server = Server(
    name = name,
    address = URL(address),
    location = Location(
        latitude = latitude,
        longitude = longitude,
    ),
)
