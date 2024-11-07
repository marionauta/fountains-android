package mn.openlocations.domain.models

import mn.openlocations.data.models.FountainPropertiesDto

data class FountainProperties(
    val bottle: BasicValue,
    override val wheelchair: WheelchairValue,
    override val mapillaryId: String?,
    override val checkDate: PortableDate?,
) : AmenityProperties

fun FountainPropertiesDto.intoDomain(): FountainProperties = FountainProperties(
    bottle = parseBasic(bottle),
    wheelchair = parseWheelchair(wheelchair),
    mapillaryId = mapillaryId,
    checkDate = checkDate?.parsePortableDate(),
)
