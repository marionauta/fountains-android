package mn.openlocations.data.models

@kotlinx.serialization.Serializable
data class AreaOsm(
    val osm_type: String,
    val osm_id: Long,
    val lat: String,
    val lon: String,
    val display_name: String,
    val importance: Double,
) {
    fun areaId(): Long? {
        return when (osm_type) {
            "way" -> osm_id + 2400000000
            "relation" -> osm_id + 3600000000
            else -> null
        }
    }
}
