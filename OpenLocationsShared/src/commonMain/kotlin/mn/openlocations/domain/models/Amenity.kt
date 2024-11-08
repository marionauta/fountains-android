package mn.openlocations.domain.models

import mn.openlocations.data.models.OverpassNode

sealed class Amenity {
    abstract val id: String
    abstract val name: String
    abstract val location: Location
    abstract val properties: AmenityProperties

    data class Fountain(
        override val id: String,
        override val name: String,
        override val location: Location,
        override val properties: FountainProperties,
    ) : Amenity()

    data class Restroom(
        override val id: String,
        override val name: String,
        override val location: Location,
        override val properties: RestroomProperties,
    ) : Amenity()
}

fun OverpassNode.intoDomain(): Amenity? {
    return when (tags["amenity"]) {
        "drinking_water" -> Amenity.Fountain(
            id = id.toString(),
            name = tags["name"] ?: "",
            location = Location(lat, lon),
            properties = tags.toFountainProperties()
        )

        "toilets" -> Amenity.Restroom(
            id = id.toString(),
            name = tags["name"] ?: "",
            location = Location(lat, lon),
            properties = tags.toRestroomProperties()
        )

        else -> null
    }
}