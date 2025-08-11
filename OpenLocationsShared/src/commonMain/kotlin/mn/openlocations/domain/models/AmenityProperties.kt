package mn.openlocations.domain.models

sealed interface AmenityProperties {
    val fee: FeeValue
    val access: AccessValue
    val wheelchair: WheelchairValue
    val imageIds: List<Pair<ImageSource, String>>
    val checkDate: PortableDate?
    val closed: Boolean
}

fun Map<String, String>.intoImageIds(): List<Pair<ImageSource, String>> {
    return listOf(
        ImageSource.Panoramax to "panoramax",
        ImageSource.Mapillary to "mapillary",
        ImageSource.Url to "image",
    ).flatMap { (source, prefix) ->
        mapNotNull { (key, value) ->
            if (!key.startsWith(prefix)) return@mapNotNull null
            return@mapNotNull source to value
        }
    }
}
