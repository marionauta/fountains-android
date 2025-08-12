package mn.openlocations.data.routes

import mn.openlocations.domain.models.FeedbackState
import mn.openlocations.networking.ApiRoute

internal class FeedbackRoute(
    osmId: String,
    state: FeedbackState,
    comment: String,
    authorId: String,
) : ApiRoute {
    override val route: String = "/v1/feedback"
    override val headers: Map<String, String> = mapOf(
        "Authorization" to "Id $authorId"
    )
    override val parameters = mapOf(
        "osm_id" to osmId,
        "state" to state,
        "comment" to comment,
    )
}
