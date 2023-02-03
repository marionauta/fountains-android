package mn.openlocations.domain.models

import mn.openlocations.data.models.ServerInfoDto
import java.net.URL

data class ServerInfo(
    val address: URL,
    val area: ServerInfoArea,
)

fun ServerInfoDto.intoDomain(address: URL): ServerInfo = ServerInfo(
    address = address,
    area = area.intoDomain(),
)
