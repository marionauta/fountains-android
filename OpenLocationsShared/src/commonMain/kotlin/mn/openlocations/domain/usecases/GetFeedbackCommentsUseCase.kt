package mn.openlocations.domain.usecases

import mn.openlocations.data.datasources.FeedbackDataSource
import mn.openlocations.data.models.FeedbackCommentDto
import mn.openlocations.domain.models.FeedbackComment
import mn.openlocations.domain.models.intoDomain
import kotlin.native.ObjCName

class GetFeedbackCommentsUseCase {
    private val dataSource = FeedbackDataSource()

    @ObjCName("callAsFunction")
    suspend operator fun invoke(osmId: String): List<FeedbackComment> {
        return dataSource.getComments(osmId).mapNotNull(FeedbackCommentDto::intoDomain)
    }
}
