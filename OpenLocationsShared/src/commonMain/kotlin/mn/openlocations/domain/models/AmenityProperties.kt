package mn.openlocations.domain.models

sealed interface AmenityProperties {
    val fee: FeeValue
    val wheelchair: WheelchairValue
    val mapillaryId: String?
    val checkDate: PortableDate?
}