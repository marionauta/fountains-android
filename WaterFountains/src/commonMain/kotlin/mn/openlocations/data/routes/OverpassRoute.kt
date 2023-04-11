package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

data class OverpassRoute(val areaId: Long) : ApiRoute {
    override val route: String = "interpreter"

    override val headers: Map<String, String> = emptyMap()

    override val parameters: Map<String, String> = mapOf(
        Pair(
            "data",
            """
            [out:json];
            area($areaId);
            node[amenity=drinking_water](area);
            out;
            """.trimIndent()
        ),
    )
}

data class OverpassGeoRoute(
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
            node[amenity=drinking_water](${south},${west},${north},${east});
            out;
            """.trimIndent()
        ),
    )
}
