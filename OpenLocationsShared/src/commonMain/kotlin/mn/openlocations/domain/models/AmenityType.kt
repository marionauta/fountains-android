package mn.openlocations.domain.models

import mn.openlocations.data.routes.OverpassFilter

enum class AmenityType {
    DrinkingFountain,
    Restroom;

    internal fun intoOverpassFilters(): Collection<OverpassFilter> {
        return when (this) {
            DrinkingFountain -> listOf(
                OverpassFilter("amenity" to "drinking_water"),
                OverpassFilter("amenity" to "fountain", "drinking_water" to "yes"),
            )

            Restroom -> listOf(
                OverpassFilter("amenity" to "toilets"),
            )
        }
    }
}
