package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.AmenityDataSource
import mn.openlocations.domain.models.AmenitiesResponse
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.models.intoDomain
import mn.openlocations.domain.models.toPortableDate

object AmenityRepository {
    private val dataSource = AmenityDataSource

    suspend fun inside(
        northEast: Location,
        southWest: Location,
        languages: List<String>,
    ): AmenitiesResponse? {
        return dataSource.inside(
            north = northEast.latitude,
            east = northEast.longitude,
            south = southWest.latitude,
            west = southWest.longitude,
        )?.let { cache ->
            AmenitiesResponse(
                lastUpdated = cache.lastUpdated.toPortableDate(),
                amenities = cache.amenities.mapNotNull { it.value.intoDomain(languages) },
            )
        }
    }

    fun get(amenityId: String, languages: List<String>): Amenity? {
        return dataSource.get(amenityId)?.intoDomain(languages)
    }
}
