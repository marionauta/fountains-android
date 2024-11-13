package mn.openlocations.domain.usecases

import mn.openlocations.data.datasources.FeedbackDataSource

class SendFeedbackUseCase {
    private val dataSource = FeedbackDataSource()

    suspend operator fun invoke(osmId: String, good: Boolean, comment: String) {
        return callAsFunction(osmId = osmId, good = good, comment = comment)
    }

    suspend fun callAsFunction(osmId: String, good: Boolean, comment: String) {
        return dataSource.report(osmId = osmId, good = good, comment = comment)
    }
}
