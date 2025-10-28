package mn.openlocations.domain.models

enum class ImageSource {
    Panoramax,
    Mapillary,
    WikimediaCommons,
    Url,
}

val ImageSource.prefix: String
    get() = when (this) {
        ImageSource.Panoramax -> "panoramax"
        ImageSource.Mapillary -> "mapillary"
        ImageSource.WikimediaCommons -> "wikimedia_commons"
        ImageSource.Url -> "image"
    }
