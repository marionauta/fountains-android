package mn.openlocations.data.routes

import mn.openlocations.networking.ApiRoute

internal class FeedbackRoute(osmId: String, state: State, comment: String) : ApiRoute {
    enum class State {
        Good,
        Bad,
    }

    override val route: String = "/v1/feedback"
    override val headers: Map<String, String> = emptyMap()
    override val parameters: Map<String, String> = mapOf(
        "osm_id" to osmId,
        "state" to state.toString().lowercase(),
        "comment" to comment,
    )
}
