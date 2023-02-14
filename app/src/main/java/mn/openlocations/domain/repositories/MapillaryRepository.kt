package mn.openlocations.domain.repositories

import mn.openlocations.BuildConfig
import mn.openlocations.data.datasources.MapillaryDataSource

class MapillaryRepository {
    companion object {
        private var token = BuildConfig.MAPILLARY_TOKEN
    }

    private val dataSource = MapillaryDataSource(token = token)

    suspend fun getImageUrl(mapillaryId: String): String? {
        return dataSource.getImage(mapillaryId)
    }
}
