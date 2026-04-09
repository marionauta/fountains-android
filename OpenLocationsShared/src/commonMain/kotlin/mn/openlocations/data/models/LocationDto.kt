package mn.openlocations.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val latitude: Double,
    val longitude: Double,
) {
    fun isInside(bounds: LocationBounds): Boolean {
        return bounds.north > latitude && latitude > bounds.south
                && bounds.west < longitude && longitude < bounds.east
    }
}
