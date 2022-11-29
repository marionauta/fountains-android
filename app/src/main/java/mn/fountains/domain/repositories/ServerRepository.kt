package mn.fountains.domain.repositories

import mn.fountains.domain.models.Server
import java.net.URL

private val servers = mutableListOf<Server>()

class ServerRepository {
    fun add(server: Server) {
        servers.add(server)
    }

    fun remove(server: Server) {
        servers.remove(server)
    }

    fun get(address: URL): Server? {
        return servers.firstOrNull { it.address == address }
    }

    fun all(): List<Server> {
        return servers
    }
}
