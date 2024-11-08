package mn.openlocations.domain.models

import mn.openlocations.domain.utils.parseBasic
import mn.openlocations.domain.utils.parseFee
import mn.openlocations.domain.utils.parsePortableDate
import mn.openlocations.domain.utils.parseWheelchair

data class RestroomProperties(
    val changingTable: BasicValue,
    val handwashing: BasicValue,
    val gender: Gender,
    override val fee: FeeValue,
    override val wheelchair: WheelchairValue,
    override val mapillaryId: String?,
    override val checkDate: PortableDate?,
) : AmenityProperties

fun Map<String, String>.toRestroomProperties(): RestroomProperties {
    return RestroomProperties(
        changingTable = get("changing_table").parseBasic(),
        handwashing = get("toilets:handwashing").parseBasic(),
        gender = Gender(
            male = get("male").parseBasic(),
            female = get("female").parseBasic(),
            unisex = get("unisex").parseBasic(),
        ),
        fee = get("fee").parseFee(amount = get("charge")),
        wheelchair = get("wheelchair").parseWheelchair(),
        mapillaryId = get("mapillary"),
        checkDate = get("check_date").parsePortableDate(),
    )
}
