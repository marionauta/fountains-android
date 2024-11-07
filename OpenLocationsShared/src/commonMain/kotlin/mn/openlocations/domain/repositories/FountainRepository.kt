package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.FountainDataSource
import mn.openlocations.domain.models.AmenitiesResponse
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.models.intoDomain

class FountainRepository {
    private val dataSource = FountainDataSource()

    suspend fun inside(northEast: Location, southWest: Location): AmenitiesResponse? {
        return dataSource.inside(
            north = northEast.latitude,
            east = northEast.longitude,
            south = southWest.latitude,
            west = southWest.longitude,
        )?.intoDomain()
    }

    fun get(fountainId: String): Amenity? = dataSource.get(fountainId)?.intoDomain()
}
