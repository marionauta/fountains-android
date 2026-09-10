package mn.openlocations.domain.models

data class FilterSettings(
    val amenities: Set<AmenityType>,
) {
    companion object {
        val default: FilterSettings = FilterSettings(
            amenities = AmenityType.entries.toSet()
        )
    }

    val producesEmptyResult: Boolean
        get() = amenities.isEmpty()

    val count: UInt
        get() {
            var res  = 0u
            if (amenities != default.amenities) res += 1u
            return res
        }
}
