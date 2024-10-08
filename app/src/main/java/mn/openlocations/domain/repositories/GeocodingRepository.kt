package mn.openlocations.domain.repositories

import mn.openlocations.BuildConfig
import mn.openlocations.data.datasources.GeocodingDataSource
import mn.openlocations.data.models.LocationDto
import mn.openlocations.domain.models.Location

class GeocodingRepository {
    private val dataSource = GeocodingDataSource(apiKey = BuildConfig.MAPS_CO_API_KEY)

    suspend fun reverse(coordinate: Location): String? {
        return dataSource.reverse(
            LocationDto(
                latitude = coordinate.latitude,
                longitude = coordinate.longitude
            )
        )
    }
}
