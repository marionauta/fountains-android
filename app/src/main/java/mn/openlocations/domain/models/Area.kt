package mn.openlocations.domain.models

import mn.openlocations.data.models.AreaOsm
import mn.openlocations.data.utils.trimmedDisplayName

data class Area(
    val id: String,
    val name: String,
    val location: Location,
    val osmAreaId: Long,
) {
    val trimmedDisplayName: String = trimmedDisplayName(name)
}

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
