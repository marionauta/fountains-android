package mn.fountains.domain.models

import mn.fountains.data.models.FountainDto

data class Fountain(
    val id: String,
    val name: String,
    val location: Location,
    val properties: FountainProperties,
)

fun FountainDto.intoDomain(): Fountain = Fountain(
    id = id,
    name = name,
    location = location.intoDomain(),
    properties = properties.intoDomain(),
)
