package mn.openlocations.data.models

@kotlinx.serialization.Serializable
data class OverpassBounds(
    val minlat: Double,
    val minlon: Double,
    val maxlat: Double,
    val maxlon: Double,
) {
    val center: LocationDto
        get() = LocationDto(
            latitude = (minlat + maxlat) / 2,
            longitude = (minlon + maxlon) / 2,
        )
}
