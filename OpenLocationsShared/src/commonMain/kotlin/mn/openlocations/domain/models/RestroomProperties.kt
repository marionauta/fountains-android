package mn.openlocations.domain.models

import mn.openlocations.domain.utils.parseAccess
import mn.openlocations.domain.utils.parseBasic
import mn.openlocations.domain.utils.parseFee
import mn.openlocations.domain.utils.parsePortableDate
import mn.openlocations.domain.utils.parseWheelchair

data class RestroomProperties(
    val changingTable: BasicValue,
    val handwashing: BasicValue,
    val gender: Gender,
    override val fee: FeeValue,
    override val access: AccessValue,
    override val wheelchair: WheelchairValue,
    override val imageIds: List<Pair<ImageSource, String>>,
    override val checkDate: PortableDate?,
    override val closed: Boolean,
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
        access = get("access").parseAccess(),
        wheelchair = get("wheelchair").parseWheelchair(),
        imageIds = intoImageIds(),
        checkDate = get("check_date").parsePortableDate(),
        closed = get("opening_hours") == "closed" || any { it.key.startsWith("disused") },
    )
}
