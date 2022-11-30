package mn.fountains.domain.models

import mn.fountains.data.models.FountainPropertiesDto

data class FountainProperties(
    val bottle: BasicValue,
    val wheelchair: WheelchairValue,
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
)
