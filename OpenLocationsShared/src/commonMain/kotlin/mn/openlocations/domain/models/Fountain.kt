package mn.openlocations.domain.models

import mn.openlocations.data.models.FountainDto

data class Fountain(
    override val id: String,
    override val name: String,
    override val location: Location,
    val properties: FountainProperties,
): Amenity

fun FountainDto.intoDomain(): Fountain = Fountain(
    id = id,
    name = name,
    location = location.intoDomain(),
    properties = properties.intoDomain(),
)
