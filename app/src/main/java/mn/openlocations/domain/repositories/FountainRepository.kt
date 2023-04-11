package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.FountainDataSource
import mn.openlocations.domain.models.*

class FountainRepository {
    private val dataSource = FountainDataSource()

    private suspend fun all(areaId: Long): FountainsResponse? {
        return dataSource.all(areaId = areaId)?.intoDomain()
    }

    suspend fun all(area: Area): FountainsResponse? {
        return all(areaId = area.osmAreaId)
    }

    suspend fun inside(northEast: Location, southWest: Location): FountainsResponse? {
        return dataSource.inside(
            north = northEast.latitude,
            east = northEast.longitude,
            south = southWest.latitude,
            west = southWest.longitude,
        )?.intoDomain()
    }

    fun get(fountainId: String): Fountain? = dataSource.get(fountainId)?.intoDomain()
}
