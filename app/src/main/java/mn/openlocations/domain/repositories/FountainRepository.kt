package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.FountainDataSource
import mn.openlocations.domain.models.Fountain
import mn.openlocations.domain.models.FountainsResponse
import mn.openlocations.domain.models.Area
import mn.openlocations.domain.models.intoDomain
import java.net.URL

class FountainRepository {
    private val dataSource = FountainDataSource()

    private suspend fun all(areaId: Long): FountainsResponse? {
        return dataSource.all(areaId = areaId)?.intoDomain()
    }

    suspend fun all(area: Area): FountainsResponse? {
        return all(areaId = area.osmAreaId)
    }

    fun get(fountainId: String): Fountain? = dataSource.get(fountainId)?.intoDomain()
}
