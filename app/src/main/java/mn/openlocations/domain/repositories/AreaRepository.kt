package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.StoredAreasDataSource
import mn.openlocations.data.models.StoredArea
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.models.Area

class AreaRepository {
    private val dataSource = StoredAreasDataSource()

    suspend fun add(area: Area) {
        val payload = StoredArea().apply {
            name = area.name
            latitude = area.location.latitude
            longitude = area.location.longitude
            osmAreaId = area.osmAreaId
        }
        dataSource.add(payload)
    }

    suspend fun get(id: String): Area? {
        return dataSource.get(id = id)?.let {
            Area(
                id = it.id,
                name = it.name,
                location = Location(
                    latitude = it.latitude,
                    longitude = it.longitude,
                ),
                osmAreaId = it.osmAreaId,
            )
        }
    }

    suspend fun all(): List<Area> {
        return dataSource.all().map {
            Area(
                id = it.id,
                name = it.name,
                location = Location(
                    latitude = it.latitude,
                    longitude = it.longitude,
                ),
                osmAreaId = it.osmAreaId,
            )
        }
    }

    suspend fun delete(area: Area) {
        dataSource.delete(id = area.id)
    }
}
