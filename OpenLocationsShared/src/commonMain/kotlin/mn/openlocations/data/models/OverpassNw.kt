package mn.openlocations.data.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed interface OverpassNw {
    val id: Long
    val location: LocationDto
    val tags: Map<String, String>

    @Serializable
    @SerialName("node")
    data class Node(
        override val id: Long,
        val lat: Double,
        val lon: Double,
        override val tags: Map<String, String>
    ) : OverpassNw {
        override val location: LocationDto
            get() = LocationDto(lat, lon)
    }

    @Serializable
    @SerialName("way")
    data class Way(
        override val id: Long,
        val bounds: OverpassBounds,
        override val tags: Map<String, String>,
    ) : OverpassNw {
        override val location: LocationDto
            get() = bounds.center
    }
}
