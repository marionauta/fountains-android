package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

internal class FeedbackCommentsRoute(val osmId: String): ApiRoute {
    override val route: String = "/v1/feedback/$osmId"
    override val headers: Map<String, String> = emptyMap()
    override val parameters: Map<String, String> = emptyMap()
}
