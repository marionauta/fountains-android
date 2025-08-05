package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.MapillaryDataSource
import mn.openlocations.domain.models.MapillaryImageData
import mn.openlocations.domain.models.intoDomain

class MapillaryRepository(mapillaryToken: String) {
    private val dataSource = MapillaryDataSource(token = mapillaryToken)

    suspend fun getImageUrl(mapillaryId: String): MapillaryImageData? {
        return dataSource.getImageData(mapillaryId)?.intoDomain()
    }
}
