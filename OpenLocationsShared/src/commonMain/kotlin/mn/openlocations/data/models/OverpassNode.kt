package mn.openlocations.data.models

@kotlinx.serialization.Serializable
data class OverpassNode(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val tags: Map<String, String>,
)