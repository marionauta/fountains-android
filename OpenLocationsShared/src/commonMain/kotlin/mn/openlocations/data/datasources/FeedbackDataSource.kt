package mn.openlocations.data.datasources

import mn.openlocations.data.routes.FeedbackRoute
import mn.openlocations.domain.models.FeedbackState
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris

internal class FeedbackDataSource {
    private val client = ApiClient(baseUrl = KnownUris.reporting.toString())

    suspend fun report(osmId: String, state: FeedbackState, comment: String, authorId: String) {
        client.form(
            route = FeedbackRoute(
                osmId = osmId,
                state = state,
                comment = comment,
                authorId = authorId,
            )
        )
    }
}
