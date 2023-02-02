package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.ServerInfoDataSource
import mn.openlocations.domain.models.ServerInfo
import mn.openlocations.domain.models.intoDomain
import java.net.URL

class ServerInfoRepository {
    private val dataSource = ServerInfoDataSource()

    suspend fun get(baseUrl: URL): ServerInfo? {
        return dataSource.get(baseUrl)?.intoDomain()
    }
}
