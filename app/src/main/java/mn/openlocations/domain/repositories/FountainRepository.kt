package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.FountainDataSource
import mn.openlocations.domain.models.Fountain
import mn.openlocations.domain.models.FountainsResponse
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.models.intoDomain

class FountainRepository {
    private val dataSource = FountainDataSource()

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
