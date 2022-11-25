package mn.fountains.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse<T>(val data: T)
