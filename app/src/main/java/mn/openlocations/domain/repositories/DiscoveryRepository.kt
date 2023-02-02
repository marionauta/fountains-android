package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.DiscoveryDataSource
import mn.openlocations.data.models.ServerDiscoveryItemDto
import mn.openlocations.domain.models.ServerDiscoveryItem
import mn.openlocations.domain.models.intoDomain

class DiscoveryRepository {
    private val dataSource = DiscoveryDataSource()

    suspend fun all(): List<ServerDiscoveryItem> {
        return dataSource.all().map(ServerDiscoveryItemDto::intoDomain)
    }
}
