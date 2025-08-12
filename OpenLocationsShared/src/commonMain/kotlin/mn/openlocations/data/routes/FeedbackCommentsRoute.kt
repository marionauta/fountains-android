package mn.openlocations.data.routes

import mn.openlocations.networking.BaseRoute

internal class FeedbackCommentsRoute(
    osmId: String,
    authorId: String,
) : BaseRoute(
    route = "/v1/feedback/$osmId",
    headers = mapOf(
        "Authorization" to "Id $authorId",
    ),
)
