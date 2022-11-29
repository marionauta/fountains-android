package mn.fountains.domain.repositories

import mn.fountains.data.datasources.FountainDataSource
import mn.fountains.data.models.FountainDto
import mn.fountains.domain.models.Fountain
import mn.fountains.domain.models.Server
import mn.fountains.domain.models.intoDomain
import java.net.URL

class FountainRepository {
    private val dataSource = FountainDataSource()

    suspend fun all(url: URL): List<Fountain>? {
        return dataSource.all(url = url)?.map(FountainDto::intoDomain)
    }

    suspend fun all(server: Server): List<Fountain>? {
        return all(url = server.address)
    }
}
