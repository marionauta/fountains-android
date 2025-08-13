package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.AmenityDataSource
import mn.openlocations.data.models.OverpassNode
import mn.openlocations.domain.models.AccessValue
import mn.openlocations.domain.models.AmenitiesResponse
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.models.intoDomain
import mn.openlocations.domain.models.toPortableDate

object AmenityRepository {
    private val dataSource = AmenityDataSource

    suspend fun inside(northEast: Location, southWest: Location): AmenitiesResponse? {
        return dataSource.inside(
            north = northEast.latitude,
            east = northEast.longitude,
            south = southWest.latitude,
            west = southWest.longitude,
        )?.let {
            AmenitiesResponse(
                lastUpdated = it.lastUpdated.toPortableDate(),
                amenities = it.amenities
                    .mapNotNull(OverpassNode::intoDomain)
                    .filter { amenity ->
                        !listOf(
                            AccessValue.No,
                            AccessValue.Private
                        ).contains(amenity.properties.access)
                    },
            )
        }
    }

    fun get(amenityId: String): Amenity? = dataSource.get(amenityId)?.intoDomain()
}
