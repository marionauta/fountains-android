package mn.openlocations.domain.models

import mn.openlocations.data.models.LocationDto

data class Location(
    val latitude: Double,
    val longitude: Double,
)

fun LocationDto.intoDomain(): Location = Location(
    latitude = latitude,
    longitude = longitude,
)
