package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.MapillaryDataSource

class MapillaryRepository {
    private val dataSource = MapillaryDataSource()

    suspend fun getImageUrl(mapillaryId: String): String? {
        return dataSource.getImage(mapillaryId)
    }
}
