package mn.fountains.domain.repositories

import mn.fountains.data.datasources.FountainDataSource
import mn.fountains.data.models.FountainDto
import mn.fountains.data.models.FountainsResponseDto
import mn.fountains.domain.models.Fountain
import mn.fountains.domain.models.FountainsResponse
import mn.fountains.domain.models.Server
import mn.fountains.domain.models.intoDomain
import java.net.URL

class FountainRepository {
    private val dataSource = FountainDataSource()

    suspend fun all(url: URL): FountainsResponse? {
        return dataSource.all(url = url)?.intoDomain()
    }

    suspend fun all(server: Server): FountainsResponse? {
        return all(url = server.address)
    }

    fun get(fountainId: String): Fountain? = dataSource.get(fountainId)?.intoDomain()
}
