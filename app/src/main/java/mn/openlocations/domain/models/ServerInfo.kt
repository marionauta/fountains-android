package mn.openlocations.domain.models

import mn.openlocations.data.models.ServerInfoDto

data class ServerInfo(
    val area: ServerInfoArea,
)

fun ServerInfoDto.intoDomain(): ServerInfo = ServerInfo(
    area = area.intoDomain(),
)
