package mn.openlocations.domain.models

data class FilterSettings(
    val amenities: Set<AmenityType>,
) {
    companion object {
        val default: FilterSettings = FilterSettings(
            amenities = AmenityType.entries.toSet()
        )
    }

    val count: Int
        get() {
            var res = 0
            if (amenities != default.amenities) res += 1
            return res
        }
}
