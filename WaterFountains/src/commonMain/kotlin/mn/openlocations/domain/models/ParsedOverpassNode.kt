package mn.openlocations.domain.models

import mn.openlocations.data.models.FountainPropertiesDto
import mn.openlocations.data.models.OverpassNode

sealed interface ParsedOverpassNode {
    val id: String
    val name: String
    val geopoint: Geopoint
    val mapillaryId: String?
}

data class FountainNode(
    override val id: String,
    override val name: String,
    override val geopoint: Geopoint,
    val properties: FountainProperties,
): ParsedOverpassNode {
    override val mapillaryId: String? = properties.mapillaryId
}

data class RestroomNode(
    override val id: String,
    override val name: String,
    override val geopoint: Geopoint,
    override val mapillaryId: String?,
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
            properties = FountainPropertiesDto(
                bottle = tags["bottle"] ?: "unknown",
                wheelchair = tags["wheelchair"] ?: "unknown",
                mapillaryId = tags["mapillary"],
                checkDate = tags["check_date"],
            ).intoDomain()
        )
        "toilets" -> RestroomNode(
            id = id.toString(),
            name = tags["name"] ?: "",
            geopoint = Geopoint(
                latitude = lat,
                longitude = lon,
            ),
            mapillaryId = tags["mapillary"],
        )
         else -> null
    }
}
