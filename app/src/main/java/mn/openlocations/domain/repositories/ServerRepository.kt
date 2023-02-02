package mn.openlocations.domain.repositories

import android.content.Context
import mn.openlocations.data.datasources.ServerDataSource
import mn.openlocations.data.models.ServerEntity
import mn.openlocations.domain.models.Server
import mn.openlocations.domain.models.intoDomain
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
