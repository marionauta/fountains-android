package mn.openlocations.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FountainDto(
    val id: String,
    val name: String,
    val location: LocationDto,
    val properties: FountainPropertiesDto,
)

fun OverpassNode.intoFountainDto(): FountainDto {
    return FountainDto(
        id = id.toString(),
        name = tags["name"] ?: "",
        location = LocationDto(
            latitude = lat,
            longitude = lon,
        ),
        properties = FountainPropertiesDto(
            bottle = tags["bottle"] ?: "unknown",
            wheelchair = tags["wheelchair"] ?: "unknown",
            mapillaryId = tags["mapillary"],
            checkDate = tags["check_date"],
        )
    )
}