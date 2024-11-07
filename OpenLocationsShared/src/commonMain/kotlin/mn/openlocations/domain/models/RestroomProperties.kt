package mn.openlocations.domain.models

data class RestroomProperties(
    val changingTable: BasicValue,
    override val wheelchair: WheelchairValue,
    override val mapillaryId: String?,
    override val checkDate: PortableDate?,
) : AmenityProperties

fun Map<String, String>.toRestroomProperties(): RestroomProperties {
    return RestroomProperties(
        changingTable = parseBasic(get("changing_table")),
        wheelchair = parseWheelchair(get("wheelchair")),
        mapillaryId = get("mapillary"),
        checkDate = get("check_date")?.parsePortableDate(),
    )
}
