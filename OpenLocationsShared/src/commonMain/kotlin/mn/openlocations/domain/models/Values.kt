package mn.openlocations.domain.models

enum class BasicValue {
    UNKNOWN, NO, YES
}

enum class WheelchairValue {
    UNKNOWN, NO, LIMITED, YES
}

sealed interface FeeValue {
    data object Unknown : FeeValue
    data object No : FeeValue
    data object Donation : FeeValue
    data class Yes(val amount: String?) : FeeValue

    val title: String
        get() = when (this) {
            is Yes -> if (amount.isNullOrBlank()) "Yes" else amount
            else -> toString()
        }
}

data class Gender(
    val male: BasicValue,
    val female: BasicValue,
    val unisex: BasicValue,
)
