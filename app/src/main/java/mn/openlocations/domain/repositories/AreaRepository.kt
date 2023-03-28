package mn.openlocations.domain.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mn.openlocations.data.datasources.NominatimDataSource
import mn.openlocations.data.datasources.StoredAreasDataSource
import mn.openlocations.data.models.AreaOsm
import mn.openlocations.data.models.StoredArea
import mn.openlocations.domain.models.Area
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.models.intoDomain

class AreaRepository {
    private val searchDataSource = NominatimDataSource()
    private val storedDataSource = StoredAreasDataSource()

    suspend fun add(area: Area) {
        val payload = StoredArea().apply {
            name = area.name
            latitude = area.location.latitude
            longitude = area.location.longitude
            osmAreaId = area.osmAreaId
        }
        storedDataSource.add(payload)
    }

    suspend fun get(id: String): Area? {
        return storedDataSource.get(id = id)?.let {
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

    fun allStream(): Flow<List<Area>> {
        return storedDataSource.allStream().map { list ->
            list.map {
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
    }

    suspend fun delete(area: Area) {
        storedDataSource.delete(id = area.id)
    }

    suspend fun search(name: String): List<Area>? {
        return searchDataSource.search(name = name)?.mapNotNull(AreaOsm::intoDomain)
    }
}
