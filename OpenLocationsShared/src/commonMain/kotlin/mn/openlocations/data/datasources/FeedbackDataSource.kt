package mn.openlocations.data.datasources

import mn.openlocations.data.models.FeedbackCommentDto
import mn.openlocations.data.models.FeedbackCommentsResponseDto
import mn.openlocations.data.routes.FeedbackCommentsRoute
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

    suspend fun getComments(osmId: String): List<FeedbackCommentDto> {
        val response = client.get<FeedbackCommentsResponseDto>(FeedbackCommentsRoute(osmId))
        return response?.data ?: emptyList()
    }
}
