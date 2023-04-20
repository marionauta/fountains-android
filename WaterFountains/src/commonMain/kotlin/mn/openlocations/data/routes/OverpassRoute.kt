package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

data class OverpassRoute(
    val amenities: List<String> = listOf(
        "drinking_water",
        "toilets",
    ),
    val north: Double,
    val east: Double,
    val south: Double,
    val west: Double,
) : ApiRoute {
    override val route: String = "interpreter"

    override val headers: Map<String, String> = emptyMap()

    override val parameters: Map<String, String> = mapOf(
        Pair(
            "data",
            """
            [out:json];
            node[amenity~"(${amenities.joinToString("|")})"](${south},${west},${north},${east});
            out;
            """.trimIndent(),
        ),
    )
}
