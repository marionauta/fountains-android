package mn.fountains.domain.models

import java.net.URL

data class Server(
    val name: String,
    val address: URL,
    val location: Location,
)
