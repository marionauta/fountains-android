package mn.fountains.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FountainsResponse(
    val fountains: List<FountainDto>,
)
