package mn.fountains.domain.repositories

import mn.fountains.data.datasources.DiscoveryDataSource
import mn.fountains.data.models.ServerDiscoveryItemDto
import mn.fountains.domain.models.ServerDiscoveryItem
import mn.fountains.domain.models.intoDomain

class DiscoveryRepository {
    private val dataSource = DiscoveryDataSource()

    suspend fun all(): List<ServerDiscoveryItem> {
        return dataSource.all().map(ServerDiscoveryItemDto::intoDomain)
    }
}
