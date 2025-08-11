package mn.openlocations.domain.models

import mn.openlocations.domain.utils.parseAccess
import mn.openlocations.domain.utils.parseBasic
import mn.openlocations.domain.utils.parseFee
import mn.openlocations.domain.utils.parsePortableDate
import mn.openlocations.domain.utils.parseWheelchair

data class FountainProperties(
    val bottle: BasicValue,
    override val fee: FeeValue,
    override val access: AccessValue,
    override val wheelchair: WheelchairValue,
    override val mapillaryId: String?,
    override val checkDate: PortableDate?,
    override val closed: Boolean,
) : AmenityProperties

fun Map<String, String>.toFountainProperties(): FountainProperties = FountainProperties(
    bottle = get("bottle").parseBasic(),
    fee = get("fee").parseFee(amount = get("charge")),
    access = get("access").parseAccess(),
    wheelchair = get("wheelchair").parseWheelchair(),
    mapillaryId = get("mapillary"),
    checkDate = get("check_date").parsePortableDate(),
    closed = get("opening_hours") == "closed" || any { it.key.startsWith("disused") },
)
