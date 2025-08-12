package mn.openlocations.networking

open class BaseRoute(
    override val route: String,
    override val headers: Map<String, String> = emptyMap(),
    override val parameters: Map<String, Any> = emptyMap(),
) : ApiRoute
