package mn.openlocations.data.datasources

import mn.openlocations.data.routes.FeedbackRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris

internal class FeedbackDataSource {
    private val client = ApiClient(baseUrl = KnownUris.reporting)

    suspend fun report(osmId: String, good: Boolean, comment: String) {
        client.form(
            route = FeedbackRoute(
                osmId = osmId,
                state = if (good) FeedbackRoute.State.Good else FeedbackRoute.State.Bad,
                comment = comment,
            )
        )
    }
}
