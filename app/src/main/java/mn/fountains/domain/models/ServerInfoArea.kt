package mn.fountains.domain.models

import mn.fountains.data.models.ServerInfoAreaDto

data class ServerInfoArea(
    val displayName: String,
    val location: Location,
)

fun ServerInfoAreaDto.intoDomain(): ServerInfoArea = ServerInfoArea(
    displayName = displayName,
    location = location.intoDomain(),
)
