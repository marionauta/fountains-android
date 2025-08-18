package mn.openlocations.domain.models

import mn.openlocations.data.models.FeedbackCommentDto

data class FeedbackComment(
    val id: String,
    val createdAt: PortableDate,
    val state: FeedbackState,
    val comment: String,
)

fun FeedbackCommentDto.intoDomain(): FeedbackComment? {
    if (comment.isBlank()) return null
    return FeedbackComment(
        id = id,
        createdAt = createdAt.toPortableDate(),
        state = if (state == "good") FeedbackState.Good else FeedbackState.Bad,
        comment = comment.trim(),
    )
}
