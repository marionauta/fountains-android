package mn.openlocations.domain.usecases

import mn.openlocations.data.datasources.FeedbackDataSource
import mn.openlocations.data.models.FeedbackCommentDto
import mn.openlocations.domain.models.FeedbackComment
import mn.openlocations.domain.models.intoDomain
import mn.openlocations.domain.utils.SecureStringStorage
import ulid.ULID
import kotlin.native.ObjCName

class GetFeedbackCommentsUseCase(private val storage: SecureStringStorage) {
    private val dataSource = FeedbackDataSource()

    @ObjCName("callAsFunction")
    suspend operator fun invoke(osmId: String): List<FeedbackComment> {
        var token = storage["user_id"]
        if (token == null) {
            token = ULID.randomULID()
            storage["user_id"] = token
        }
        return dataSource.getComments(osmId, token).mapNotNull(FeedbackCommentDto::intoDomain)
    }
}
