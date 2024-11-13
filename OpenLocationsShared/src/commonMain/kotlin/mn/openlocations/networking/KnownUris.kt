package mn.openlocations.networking

class KnownUris {
    companion object {
        private const val website = "https://aguapp.nachbaur.dev"
        const val developer = "https://mario.nachbaur.dev?ref=AguApp"
        const val mapillary = "https://graph.mapillary.com"
        const val reporting = "http://192.168.1.133:3000/" // remove @xml/network_security_config
        const val geocoding = "https://geocode.maps.co/"
        const val overpass = "https://overpass-api.de/api"
        const val googleMaps = "https://www.google.com/maps/search/"

        fun help(slug: String): String = "$website/help/${slug}?ref=AguApp"
        fun website(): String = "$website?ref=AguApp"
    }
}
