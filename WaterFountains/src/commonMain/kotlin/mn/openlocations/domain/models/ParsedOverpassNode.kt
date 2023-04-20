package mn.openlocations.domain.models

import mn.openlocations.data.models.OverpassNode

sealed interface ParsedOverpassNode {
    val id: String
    val name: String
    val geopoint: Geopoint
}

data class FountainNode(
    override val id: String,
    override val name: String,
    override val geopoint: Geopoint,
): ParsedOverpassNode

data class RestroomNode(
    override val id: String,
    override val name: String,
    override val geopoint: Geopoint,
): ParsedOverpassNode

fun OverpassNode.intoDomain(): ParsedOverpassNode? {
    return when (tags["amenity"]) {
        "drinking_water" -> FountainNode(
            id = id.toString(),
            name = tags["name"] ?: "",
            geopoint = Geopoint(
                latitude = lat,
                longitude = lon,
            ),
        )
        "toilets" -> RestroomNode(
            id = id.toString(),
            name = tags["name"] ?: "",
            geopoint = Geopoint(
                latitude = lat,
                longitude = lon,
            ),
        )
         else -> null
    }
}
