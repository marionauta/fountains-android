package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.MapillaryDataSource
import mn.openlocations.domain.models.ImageMetadata
import mn.openlocations.domain.models.intoDomain

internal class MapillaryRepository(mapillaryToken: String) {
    private val dataSource = MapillaryDataSource(token = mapillaryToken)

    suspend fun getImageMetadata(mapillaryId: String): ImageMetadata? {
        return dataSource.getImageData(mapillaryId)?.intoDomain()
    }
}
