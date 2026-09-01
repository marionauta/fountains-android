package mn.openlocations.data.routes

import mn.openlocations.data.models.LocationBounds
import mn.openlocations.networking.ApiRoute
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class OverpassRoute(
    filters: List<Filter>,
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
            out center;
            """.trimIndent(),
        ),
    )

    override val timeout: Duration
        get() = 10.seconds

    @JvmInline
    internal value class Filter(private val value: Map<String, String>) {
        constructor(vararg pairs: Pair<String, String>) : this(mapOf(*pairs))

        // format: nw[key1=value1][key2=value2];
        override fun toString(): String {
            return "nw${value.map { "[${it.key}=${it.value}]" }.joinToString(separator = "")};"
        }
    }
}
