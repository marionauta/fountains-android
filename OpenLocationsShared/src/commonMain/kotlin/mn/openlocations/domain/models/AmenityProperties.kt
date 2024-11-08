package mn.openlocations.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

sealed interface AmenityProperties {
    val fee: FeeValue
    val wheelchair: WheelchairValue
    val mapillaryId: String?
    val checkDate: PortableDate?
}

enum class BasicValue {
    UNKNOWN, NO, YES
}

internal fun parseBasic(value: String?): BasicValue = try {
    BasicValue.valueOf(value?.uppercase() ?: "unknown")
} catch (_: IllegalArgumentException) {
    BasicValue.UNKNOWN
}

enum class WheelchairValue {
    UNKNOWN, NO, LIMITED, YES
}

internal fun parseWheelchair(value: String?): WheelchairValue = try {
    WheelchairValue.valueOf(value?.uppercase() ?: "unknown")
} catch (_: IllegalArgumentException) {
    WheelchairValue.UNKNOWN
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

internal fun String?.parseFee(amount: String?): FeeValue = when (this) {
    "no" -> FeeValue.No
    "donation" -> FeeValue.Donation
    "yes" -> FeeValue.Yes(amount)
    else -> if (amount.isNullOrBlank()) FeeValue.Unknown else FeeValue.Yes(amount)
}

internal fun String.parsePortableDate(): PortableDate? {
    return try {
        val dateTime = LocalDateTime(LocalDate.parse(this), LocalTime(12, 0, 0))
        dateTime.toInstant(TimeZone.currentSystemDefault()).toPortableDate()
    } catch (_: Exception) {
        null
    }
}