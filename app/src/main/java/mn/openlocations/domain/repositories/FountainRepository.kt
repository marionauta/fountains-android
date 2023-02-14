package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.FountainDataSource
import mn.openlocations.domain.models.Fountain
import mn.openlocations.domain.models.FountainsResponse
import mn.openlocations.domain.models.Server
import mn.openlocations.domain.models.intoDomain
import java.net.URL

class FountainRepository {
    private val dataSource = FountainDataSource()

    suspend fun all(url: URL): FountainsResponse? {
        return dataSource.all(url = url.toString())?.intoDomain()
    }

    suspend fun all(server: Server): FountainsResponse? {
        return all(url = server.address)
    }

    fun get(fountainId: String): Fountain? = dataSource.get(fountainId)?.intoDomain()
}
