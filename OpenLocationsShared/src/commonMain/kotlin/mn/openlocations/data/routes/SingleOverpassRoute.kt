package mn.openlocations.data.routes

import mn.openlocations.data.models.OsmId
import mn.openlocations.networking.ApiRoute
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class SingleOverpassRoute(val osmId: OsmId): ApiRoute {
    override val route: String = "interpreter"

    override val headers: Map<String, String>
        get() = emptyMap()

    private val specifier: String = when (osmId) {
        is OsmId.Node -> "node"
        is OsmId.Way -> "way"
    }

    override val parameters: Map<String, String> = mapOf(
        Pair(
            "data",
            """
            [out:json];
            ${specifier}(${osmId.id});
            out geom;
            """.trimIndent(),
        ),
    )

    override val timeout: Duration?
        get() = null
}
