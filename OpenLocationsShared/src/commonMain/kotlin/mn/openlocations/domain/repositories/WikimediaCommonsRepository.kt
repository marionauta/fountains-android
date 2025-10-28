package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.WikimediaCommonsDataSource
import mn.openlocations.domain.models.ImageMetadata
import mn.openlocations.domain.models.intoDomain

internal object WikimediaCommonsRepository {
    private val dataSource = WikimediaCommonsDataSource

    suspend fun getImageMetadata(name: String): ImageMetadata? {
        return dataSource.getImageData(name)?.intoDomain()
    }
}
