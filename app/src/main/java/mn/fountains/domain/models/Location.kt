package mn.fountains.domain.models

import mn.fountains.data.models.LocationDto

data class Location(
    val latitude: Double,
    val longitude: Double,
)

fun LocationDto.intoDomain(): Location = Location(
    latitude = latitude,
    longitude = longitude,
)
