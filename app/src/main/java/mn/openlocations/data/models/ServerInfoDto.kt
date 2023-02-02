package mn.openlocations.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ServerInfoDto(
    val area: ServerInfoAreaDto,
)
