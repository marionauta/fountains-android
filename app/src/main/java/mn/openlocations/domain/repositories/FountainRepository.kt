package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.FountainDataSource
import mn.openlocations.data.datasources.StoredAreasDataSource
import mn.openlocations.domain.models.FountainsResponse
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.models.ParsedOverpassNode
import mn.openlocations.domain.models.intoDomain

class FountainRepository {
    private val storedDataSource = StoredAreasDataSource()
    private val dataSource = FountainDataSource()

    suspend fun inside(northEast: Location, southWest: Location): FountainsResponse? {
        storedDataSource.deleteAll()
        return dataSource.inside(
            north = northEast.latitude,
            east = northEast.longitude,
            south = southWest.latitude,
            west = southWest.longitude,
        )?.intoDomain()
    }

    fun get(fountainId: String): ParsedOverpassNode? = dataSource.get(fountainId)?.intoDomain()
}
