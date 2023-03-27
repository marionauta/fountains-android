package mn.openlocations.data.models

@kotlinx.serialization.Serializable
data class OverpassResponse(
    val elements: List<OverpassNode>,
)
