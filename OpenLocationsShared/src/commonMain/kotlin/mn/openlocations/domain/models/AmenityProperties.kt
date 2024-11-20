package mn.openlocations.domain.models

sealed interface AmenityProperties {
    val fee: FeeValue
    val access: AccessValue
    val wheelchair: WheelchairValue
    val mapillaryId: String?
    val checkDate: PortableDate?
}
