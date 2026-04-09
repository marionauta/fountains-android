package mn.openlocations.networking

import kotlin.time.Duration

open class BaseRoute(
    override val route: String,
    override val headers: Map<String, String> = emptyMap(),
    override val parameters: Map<String, Any> = emptyMap(),
    override val timeout: Duration? = null,
) : ApiRoute
