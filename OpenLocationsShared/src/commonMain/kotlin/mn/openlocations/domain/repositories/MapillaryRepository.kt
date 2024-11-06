package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.MapillaryDataSource

class MapillaryRepository(mapillaryToken: String) {
    private val dataSource = MapillaryDataSource(token = mapillaryToken)

    suspend fun getImageUrl(mapillaryId: String): String? {
        return dataSource.getImage(mapillaryId)
    }
}
