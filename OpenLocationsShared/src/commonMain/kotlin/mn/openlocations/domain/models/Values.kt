package mn.openlocations.domain.models

enum class BasicValue {
    UNKNOWN, NO, YES
}

enum class WheelchairValue {
    UNKNOWN, NO, LIMITED, YES
}

enum class AccessValue {
    Unknown,
    No,
    Private,
    Customers,
    Permissive,
    Yes,
}

sealed interface FeeValue {
    data object Unknown : FeeValue
    data object No : FeeValue
    data object Donation : FeeValue
    data class Yes(val amount: String?) : FeeValue
}

data class Gender(
    val male: BasicValue,
    val female: BasicValue,
    val unisex: BasicValue,
)
