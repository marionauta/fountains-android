package mn.openlocations.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

data class RestroomProperties(
    val changingTable: BasicValue,
    val wheelchair: WheelchairValue,
    val mapillaryId: String?,
    val checkDate: PortableDate?,
)

fun Map<String, String>.toRestroomProperties(): RestroomProperties {
    return RestroomProperties(
        changingTable = parseBasic(this["changing_table"]),
        wheelchair = parseWheelchair(this["wheelchair"]),
        mapillaryId = this["mapillary"],
        checkDate = this["check_date"]?.let {
            try {
                val dateTime = LocalDateTime(LocalDate.parse(it), LocalTime(12, 0, 0))
                dateTime.toInstant(TimeZone.currentSystemDefault()).toPortableDate()
            } catch (e: Exception) {
                null
            }
        },
    )
}
