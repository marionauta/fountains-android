package mn.openlocations.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class OverpassResponse(
    private val osm3s: OSM3s,
    val elements: List<OverpassNw>,
) {
    fun lastUpdated(): Instant = osm3s.timestampOsmBase
}

@Serializable
data class OSM3s(
    @SerialName("timestamp_osm_base")
    val timestampOsmBase: Instant,
)
