package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.PanoramaxDataSource
import mn.openlocations.domain.models.ImageMetadata
import mn.openlocations.domain.models.intoDomain

internal object PanoramaxRepository {
    private val dataSource = PanoramaxDataSource

    suspend fun getImageMetadata(panoramaxId: String): ImageMetadata? {
        return dataSource.getImageData(panoramaxId)?.intoDomain()
    }
}
