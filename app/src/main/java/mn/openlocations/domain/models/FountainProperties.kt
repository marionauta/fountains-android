package mn.openlocations.domain.models

import kotlinx.datetime.LocalDate
import mn.openlocations.data.models.FountainPropertiesDto

data class FountainProperties(
    val bottle: BasicValue,
    val wheelchair: WheelchairValue,
    val mapillaryId: String?,
    val checkDate: LocalDate?,
)

enum class BasicValue {
    UNKNOWN, NO, YES
}

private fun parseBasic(value: String): BasicValue = try {
    BasicValue.valueOf(value.uppercase())
} catch (e: IllegalArgumentException) {
    BasicValue.UNKNOWN
}

enum class WheelchairValue {
    UNKNOWN, NO, LIMITED, YES
}

private fun parseWheelchair(value: String): WheelchairValue = try {
    WheelchairValue.valueOf(value.uppercase())
} catch (e: IllegalArgumentException) {
    WheelchairValue.UNKNOWN
}

fun FountainPropertiesDto.intoDomain(): FountainProperties = FountainProperties(
    bottle = parseBasic(bottle),
    wheelchair = parseWheelchair(wheelchair),
    mapillaryId = mapillaryId,
    checkDate = checkDate?.let {
        try {
            LocalDate.parse(it)
        } catch (e: Exception) {
            null
        }
    },
)
