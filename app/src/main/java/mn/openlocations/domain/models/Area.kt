package mn.openlocations.domain.models

import mn.openlocations.data.models.AreaOsm

data class Area(
    val id: String,
    val name: String,
    val location: Location,
    val osmAreaId: Long,
)

fun AreaOsm.intoDomain(): Area? = areaId()?.let { areaId ->
    Area(
        id = osm_id.toString(),
        name = display_name,
        location = Location(
            latitude = lat.toDouble(),
            longitude = lon.toDouble(),
        ),
        osmAreaId = areaId,
    )
}
