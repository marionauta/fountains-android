package mn.openlocations.networking

class KnownUris {
    companion object {
        const val website = "https://aguapp.nachbaur.dev?ref=AguApp"
        const val developer = "https://mario.nachbaur.dev?ref=AguApp"
        const val mapillary = "https://graph.mapillary.com"
        const val geocoding = "https://geocode.maps.co/"
        const val overpass = "https://overpass-api.de/api"

        fun help(slug: String): String = "$website/help/${slug}?ref=AguApp"
    }
}
