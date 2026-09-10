package mn.openlocations.data.routes

import mn.openlocations.data.models.LocationBounds
import mn.openlocations.networking.ApiRoute
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class OverpassRoute(
    filters: Collection<OverpassFilter>,
    bounds: LocationBounds,
) : ApiRoute {
    override val route: String = "interpreter"

    override val headers: Map<String, String>
        get() = mapOf(
            "Origin" to "https://overpass-turbo.eu",
            "Referer" to "https://overpass-turbo.eu/",
        )

    override val parameters: Map<String, String> = mapOf(
        Pair(
            "data",
            """
            [out:json][bbox:${bounds.south},${bounds.west},${bounds.north},${bounds.east}];
            (${filters.joinToString("")})->.all;
            nw.all[access!=no][access!=private];
            out center meta;
            """.trimIndent(),
        ),
    )

    override val timeout: Duration
        get() = 10.seconds
}
