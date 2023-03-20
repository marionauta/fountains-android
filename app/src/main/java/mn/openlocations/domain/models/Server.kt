package mn.openlocations.domain.models

import java.net.URL

data class Server(
    val id: String,
    val name: String,
    val address: URL,
    val location: Location,
)
