package mn.openlocations.data.models

data class LocationBounds(
    private val northEast: LocationDto,
    private val southWest: LocationDto,
) {
    val north: Double
        get() = northEast.latitude

    val east: Double
        get() = northEast.longitude

    val south: Double
        get() = southWest.latitude

    val west: Double
        get() = southWest.longitude
}
