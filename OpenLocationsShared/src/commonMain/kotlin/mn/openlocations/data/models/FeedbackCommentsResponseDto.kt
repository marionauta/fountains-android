package mn.openlocations.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackCommentsResponseDto(
    val data: List<FeedbackCommentDto>,
)
