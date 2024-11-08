package mn.openlocations.domain.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import mn.openlocations.domain.models.BasicValue
import mn.openlocations.domain.models.FeeValue
import mn.openlocations.domain.models.PortableDate
import mn.openlocations.domain.models.WheelchairValue
import mn.openlocations.domain.models.toPortableDate

internal fun String?.parseBasic(): BasicValue = try {
    BasicValue.valueOf(this?.uppercase() ?: "unknown")
} catch (_: IllegalArgumentException) {
    BasicValue.UNKNOWN
}

internal fun String?.parseWheelchair(): WheelchairValue = try {
    WheelchairValue.valueOf(this?.uppercase() ?: "unknown")
} catch (_: IllegalArgumentException) {
    WheelchairValue.UNKNOWN
}

internal fun String?.parseFee(amount: String?): FeeValue = when (this) {
    "no" -> FeeValue.No
    "donation" -> FeeValue.Donation
    "yes" -> FeeValue.Yes(amount)
    else -> if (amount.isNullOrBlank()) FeeValue.Unknown else FeeValue.Yes(amount)
}

internal fun String?.parsePortableDate(): PortableDate? {
    return this?.let { string ->
        try {
            val dateTime = LocalDateTime(LocalDate.parse(string), LocalTime(12, 0, 0))
            dateTime.toInstant(TimeZone.currentSystemDefault()).toPortableDate()
        } catch (_: Exception) {
            null
        }
    }
}
