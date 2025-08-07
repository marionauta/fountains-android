package mn.openlocations.data.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackCommentDto(
    val id: String,
    @SerialName("created_at")
    val createdAt: Instant,
    val state: String,
    val comment: String,
)
