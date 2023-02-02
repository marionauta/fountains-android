package mn.fountains.domain.models

import mn.fountains.data.models.ServerInfoDto

data class ServerInfo(
    val area: ServerInfoArea,
)

fun ServerInfoDto.intoDomain(): ServerInfo = ServerInfo(
    area = area.intoDomain(),
)
