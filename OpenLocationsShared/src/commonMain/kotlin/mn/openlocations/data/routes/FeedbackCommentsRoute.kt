package mn.openlocations.data.routes

import mn.openlocations.data.models.OsmId
import mn.openlocations.networking.BaseRoute

internal class FeedbackCommentsRoute(
    osmId: OsmId,
    authorId: String,
) : BaseRoute(
    route = "/v1/feedback/${osmId.id}",
    headers = mapOf(
        "Authorization" to "Id $authorId",
    ),
)
