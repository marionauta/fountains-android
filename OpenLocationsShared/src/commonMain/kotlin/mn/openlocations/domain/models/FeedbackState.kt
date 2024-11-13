package mn.openlocations.domain.models

enum class FeedbackState {
    Good, Bad;

    override fun toString(): String = when (this) {
        Good -> "good"
        Bad -> "bad"
    }
}
