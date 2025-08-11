package mn.openlocations.networking

import mn.openlocations.domain.models.Url
import mn.openlocations.domain.models.toPortableUrl

object KnownUris {
    private val website = "https://aguapp.nachbaur.dev".toPortableUrl()!!
    val developer = "https://mario.nachbaur.dev?ref=AguApp".toPortableUrl()!!
    val mapillary = "https://graph.mapillary.com".toPortableUrl()!!
    val reporting = "https://apiopenlocations.nachbaur.dev/".toPortableUrl()!!
    val panoramax = "https://api.panoramax.xyz".toPortableUrl()!!
    val geocoding = "https://geocode.maps.co/".toPortableUrl()!!
    val overpass = "https://overpass-api.de/api".toPortableUrl()!!
    val googleMaps: Url = "https://www.google.com/maps/search/".toPortableUrl()!!

    fun help(slug: String): Url = "$website/help/${slug}/?ref=AguApp".toPortableUrl()!!
    fun website(): Url = "$website?ref=AguApp".toPortableUrl()!!
}
