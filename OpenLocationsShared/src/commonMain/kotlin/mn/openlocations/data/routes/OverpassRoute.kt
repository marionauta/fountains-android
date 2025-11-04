package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

internal class OverpassRoute(
    amenities: List<String> = listOf(
        "drinking_water",
        "toilets",
    ),
    north: Double,
    east: Double,
    south: Double,
    west: Double,
) : ApiRoute {
    override val route: String = "interpreter"

    override val headers: Map<String, String>
        get() = emptyMap()

    override val parameters: Map<String, String> = mapOf(
        Pair(
            "data",
            """
            [out:json][bbox:${south},${west},${north},${east}];
            nw[amenity~"${amenities.joinToString("|")}"][access!=no][access!=private];
            out geom;
            """.trimIndent(),
        ),
    )
}
