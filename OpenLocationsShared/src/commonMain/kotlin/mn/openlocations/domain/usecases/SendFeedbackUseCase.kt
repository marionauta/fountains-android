package mn.openlocations.domain.usecases

import mn.openlocations.data.datasources.FeedbackDataSource
import mn.openlocations.domain.models.FeedbackState
import mn.openlocations.domain.utils.SecureStringStorage
import ulid.ULID
import kotlin.native.ObjCName

class SendFeedbackUseCase(private val storage: SecureStringStorage) {
    private val dataSource = FeedbackDataSource

    @ObjCName("callAsFunction")
    suspend operator fun invoke(payload: Payload): Boolean {
        var token = storage["user_id"]
        if (token == null) {
            token = ULID.randomULID()
            storage["user_id"] = token
        }
        return dataSource.report(
            osmId = payload.osmId,
            state = payload.state,
            comment = payload.comment,
            authorId = token
        ).isSuccess
    }

    data class Payload(
        val osmId: String,
        val state: FeedbackState,
        val comment: String,
    ) {
        var isSendEnabled: Boolean = state != FeedbackState.Bad || comment.isNotBlank()
    }
}
