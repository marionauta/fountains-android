package mn.openlocations.domain.models

data class RestroomProperties(
    val changingTable: BasicValue,
    val handwashing: BasicValue,
    override val fee: FeeValue,
    override val wheelchair: WheelchairValue,
    override val mapillaryId: String?,
    override val checkDate: PortableDate?,
) : AmenityProperties

fun Map<String, String>.toRestroomProperties(): RestroomProperties {
    return RestroomProperties(
        changingTable = parseBasic(get("changing_table")),
        handwashing = parseBasic(get("toilets:handwashing")),
        fee = get("fee").parseFee(amount = get("charge")),
        wheelchair = parseWheelchair(get("wheelchair")),
        mapillaryId = get("mapillary"),
        checkDate = get("check_date")?.parsePortableDate(),
    )
}
