package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

internal class FeedbackCommentsRoute(osmId: String, authorId: String): ApiRoute {
    override val route: String = "/v1/feedback/$osmId"
    override val headers: Map<String, String> = mapOf(
        "Authorization" to "Id $authorId"
    )
    override val parameters: Map<String, String> = emptyMap()
}
