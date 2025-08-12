package mn.openlocations.networking

import mn.openlocations.domain.models.Location
import mn.openlocations.domain.models.Url
import mn.openlocations.domain.models.toPortableUrl

object KnownUris {
    val website = "https://waterfinder.org?ref=AguApp".toPortableUrl()!!
    val developer = "https://mario.nachbaur.dev?ref=AguApp".toPortableUrl()!!
    val mapillary = "https://graph.mapillary.com".toPortableUrl()!!
    val reporting = "https://api.waterfinder.org".toPortableUrl()!!
    val panoramax = "https://api.panoramax.xyz".toPortableUrl()!!
    val geocoding = "https://geocode.maps.co/".toPortableUrl()!!
    val overpass = "https://overpass-api.de/api".toPortableUrl()!!
    private val googleMaps = "https://www.google.com/maps/search/?api=1".toPortableUrl()!!

    fun help(slug: String): Url = website
        .buildUpon()
        .appendPath("help")
        .appendPath(slug)
        .build()

    fun fix(location: Location): Url = help("fix")
        .buildUpon()
        .appendQueryParameter("lat", location.latitude.toString())
        .appendQueryParameter("lng", location.longitude.toString())
        .build()

    fun googleMaps(location: Location): Url = googleMaps
        .buildUpon()
        .appendQueryParameter("query", "${location.latitude},${location.longitude}")
        .build()
}
