package mn.openlocations.data.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class OverpassResponse(
    private val osm3s: OSM3s,
    val elements: List<OverpassNode>,
) {
    fun lastUpdated(): Instant = osm3s.timestampOsmBase
}

@kotlinx.serialization.Serializable
data class OSM3s(
    @SerialName("timestamp_osm_base")
    val timestampOsmBase: Instant,
)
