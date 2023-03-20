package mn.openlocations.domain.repositories

import mn.openlocations.data.datasources.StoredServersDataSource
import mn.openlocations.data.models.StoredServer
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.models.Server
import mn.openlocations.domain.models.intoDomain
import java.net.URL

class ServerRepository {
    private val dataSource = StoredServersDataSource()

    suspend fun add(server: Server) {
        val payload = StoredServer().apply {
            name = server.name
            address = server.address.toString()
            latitude = server.location.latitude
            longitude = server.location.longitude
        }
        dataSource.add(payload)
    }

    suspend fun get(id: String): Server? {
        return dataSource.get(id = id)?.let {
            Server(
                id = it.id,
                name = it.name,
                address = URL(it.address),
                location = Location(
                    latitude = it.latitude,
                    longitude = it.longitude,
                ),
            )
        }
    }

    suspend fun all(): List<Server> {
        return dataSource.all().map {
            Server(
                id = it.id,
                name = it.name,
                address = URL(it.address),
                location = Location(
                    latitude = it.latitude,
                    longitude = it.longitude,
                ),
            )
        }
    }

    suspend fun delete(server: Server) {
        dataSource.delete(id = server.id)
    }
}
