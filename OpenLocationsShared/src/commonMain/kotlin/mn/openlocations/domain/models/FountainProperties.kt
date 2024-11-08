package mn.openlocations.domain.models

data class FountainProperties(
    val bottle: BasicValue,
    override val fee: FeeValue,
    override val wheelchair: WheelchairValue,
    override val mapillaryId: String?,
    override val checkDate: PortableDate?,
) : AmenityProperties

fun Map<String, String>.toFountainProperties(): FountainProperties = FountainProperties(
    bottle = parseBasic(get("bottle")),
    fee = get("fee").parseFee(amount = get("charge")),
    wheelchair = parseWheelchair(get("wheelchair")),
    mapillaryId = get("mapillary"),
    checkDate = get("check_date")?.parsePortableDate(),
)
