package mn.fountains.domain.repositories

import mn.fountains.data.datasources.MapillaryDataSource

class MapillaryRepository {
    private val dataSource = MapillaryDataSource()

    suspend fun getImageUrl(mapillaryId: String): String? {
        return dataSource.getImage(mapillaryId)
    }
}
