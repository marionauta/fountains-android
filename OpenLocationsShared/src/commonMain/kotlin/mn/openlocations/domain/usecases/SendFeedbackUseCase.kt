package mn.openlocations.domain.usecases

import mn.openlocations.data.datasources.FeedbackDataSource
import mn.openlocations.domain.models.FeedbackState

class SendFeedbackUseCase {
    private val dataSource = FeedbackDataSource()

    suspend operator fun invoke(osmId: String, state: FeedbackState, comment: String) {
        return callAsFunction(osmId = osmId, state = state, comment = comment)
    }

    suspend fun callAsFunction(osmId: String, state: FeedbackState, comment: String) {
        return dataSource.report(osmId = osmId, state = state, comment = comment)
    }
}
