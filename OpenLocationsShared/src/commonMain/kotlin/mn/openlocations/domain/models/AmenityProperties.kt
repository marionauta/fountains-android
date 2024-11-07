package mn.openlocations.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

sealed interface AmenityProperties {
    val wheelchair: WheelchairValue
    val mapillaryId: String?
    val checkDate: PortableDate?
}

enum class BasicValue {
    UNKNOWN, NO, YES
}

internal fun parseBasic(value: String?): BasicValue = try {
    BasicValue.valueOf(value?.uppercase() ?: "unknown")
} catch (e: IllegalArgumentException) {
    BasicValue.UNKNOWN
}

enum class WheelchairValue {
    UNKNOWN, NO, LIMITED, YES
}

internal fun parseWheelchair(value: String?): WheelchairValue = try {
    WheelchairValue.valueOf(value?.uppercase() ?: "unknown")
} catch (e: IllegalArgumentException) {
    WheelchairValue.UNKNOWN
}

internal fun String.parsePortableDate(): PortableDate? {
    return try {
        val dateTime = LocalDateTime(LocalDate.parse(this), LocalTime(12, 0, 0))
        dateTime.toInstant(TimeZone.currentSystemDefault()).toPortableDate()
    } catch (e: Exception) {
        null
    }
}