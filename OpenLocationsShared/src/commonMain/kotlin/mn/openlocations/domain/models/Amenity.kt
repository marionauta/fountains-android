package mn.openlocations.domain.models

import mn.openlocations.data.models.OverpassNw

// Needs to be a class for iOS interop
sealed class Amenity {
    abstract val id: String
    abstract val name: String
    abstract val description: String
    abstract val location: Location
    abstract val properties: AmenityProperties

    data class Fountain(
        override val id: String,
        override val name: String,
        override val description: String,
        override val location: Location,
        override val properties: FountainProperties,
    ) : Amenity()

    data class Restroom(
        override val id: String,
        override val name: String,
        override val description: String,
        override val location: Location,
        override val properties: RestroomProperties,
    ) : Amenity()
}

fun OverpassNw.intoDomain(languages: List<String>): Amenity? {
    val name = tags.getLocalized("name", languages) ?: ""
    val description = tags.getLocalized("description", languages) ?: ""
    return when (tags["amenity"]) {
        "drinking_water" -> Amenity.Fountain(
            id = id.toString(),
            name = name,
            description = description,
            location = location.intoDomain(),
            properties = tags.toFountainProperties()
        )

        "toilets" -> Amenity.Restroom(
            id = id.toString(),
            name = name,
            description = description,
            location = location.intoDomain(),
            properties = tags.toRestroomProperties()
        )

        else -> null
    }
}

fun Map<String, String>.getLocalized(key: String, languages: List<String>): String? {
    if (languages.isEmpty()) {
        return get(key)
    }
    languages.forEach { language ->
        val key = "$key:$language"
        if (contains(key)) {
            return get(key)
        }
    }
    return get(key)
}
