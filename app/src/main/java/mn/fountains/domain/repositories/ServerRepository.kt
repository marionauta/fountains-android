package mn.fountains.domain.repositories

import mn.fountains.domain.models.Server

private val servers = mutableListOf<Server>()

class ServerRepository {
    fun add(server: Server) {
        servers.add(server)
    }

    fun remove(server: Server) {
        servers.remove(server)
    }

    fun all(): List<Server> {
        return servers
    }
}
