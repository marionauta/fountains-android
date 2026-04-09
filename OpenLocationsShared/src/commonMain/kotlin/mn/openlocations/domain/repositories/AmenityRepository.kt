package mn.openlocations.domain.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mn.openlocations.data.datasources.AmenityDataSource
import mn.openlocations.data.datasources.AmenityDataSourceCached
import mn.openlocations.data.datasources.AmenityDataSourceInMemory
import mn.openlocations.data.datasources.AmenityDataSourceOverpass
import mn.openlocations.data.models.LocationBounds
import mn.openlocations.data.models.LocationDto
import mn.openlocations.data.models.OsmId
import mn.openlocations.domain.models.AmenitiesResponse
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.models.intoDomain

object AmenityRepository {
    private val dataSource: AmenityDataSource = AmenityDataSourceOverpass
    private val cachedDataSource: AmenityDataSourceCached = AmenityDataSourceInMemory

    fun inside(
        northEast: Location,
        southWest: Location,
        languages: List<String>,
    ): Flow<AmenitiesResponse> {
        val bounds = LocationBounds(
            northEast = LocationDto(latitude = northEast.latitude, longitude = northEast.longitude),
            southWest = LocationDto(latitude = southWest.latitude, longitude = southWest.longitude),
        )
        return flow {
            val cached = cachedDataSource.inside(bounds)
            cached.onSuccess {
                if (it.isNotEmpty()) {
                    emit(it.intoDomain(languages))
                }
            }
            val nws = dataSource.inside(bounds)
            nws.onSuccess {
                cachedDataSource.save(it)
                if (it.isNotEmpty()) {
                    emit(it.intoDomain(languages))
                }
            }
        }
    }

    fun get(osmId: OsmId, languages: List<String>): Flow<Amenity> {
        return flow {
            val cached = cachedDataSource.get(osmId)
            cached?.intoDomain(languages)?.let { emit(it) }
            if (cached != null) return@flow

            val amenity = dataSource.get(osmId) ?: return@flow
            cachedDataSource.save(listOf(amenity))
            amenity.intoDomain(languages)?.let { emit(it) }
        }
    }
}
