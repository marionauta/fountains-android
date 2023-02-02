package mn.fountains.domain.repositories

import mn.fountains.data.datasources.ServerInfoDataSource
import mn.fountains.domain.models.ServerInfo
import mn.fountains.domain.models.intoDomain
import java.net.URL

class ServerInfoRepository {
    private val dataSource = ServerInfoDataSource()

    suspend fun get(baseUrl: URL): ServerInfo? {
        return dataSource.get(baseUrl)?.intoDomain()
    }
}
