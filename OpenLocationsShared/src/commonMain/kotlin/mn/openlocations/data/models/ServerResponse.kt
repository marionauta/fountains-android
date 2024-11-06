package mn.openlocations.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse<T>(val data: T)
