package mn.fountains.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ServerInfoDto(
    val area: ServerInfoAreaDto,
)
