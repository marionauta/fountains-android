package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.MapillaryDataSource
import mn.openlocations.domain.models.PortableUrl
import mn.openlocations.domain.models.toPortableUrl

class MapillaryRepository(mapillaryToken: String) {
    private val dataSource = MapillaryDataSource(token = mapillaryToken)

    suspend fun getImageUrl(mapillaryId: String): PortableUrl? {
        return dataSource.getImage(mapillaryId)?.toPortableUrl()
    }
}
