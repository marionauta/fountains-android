package mn.fountains.domain.repositories

import android.content.Context
import mn.fountains.data.datasources.ServerDataSource
import mn.fountains.data.models.LocationDto
import mn.fountains.data.models.ServerEntity
import mn.fountains.domain.models.Location
import mn.fountains.domain.models.Server
import mn.fountains.domain.models.intoDomain
import java.net.URL

class ServerRepository(private val context: Context) {
    private val dataSource = ServerDataSource()

    suspend fun add(server: Server) {
        val payload = ServerEntity(
            name = server.name,
            address = server.address.toString(),
            latitude = server.location.latitude,
            longitude = server.location.longitude,
        )
        dataSource.add(context, payload)
    }

    suspend fun get(address: URL): Server? {
        return dataSource.get(context, address.toString())?.intoDomain()
    }

    suspend fun all(): List<Server> {
        return dataSource.all(context).map(ServerEntity::intoDomain)
    }

    suspend fun delete(server: Server) {
        dataSource.delete(context, server.address.toString())
    }
}
