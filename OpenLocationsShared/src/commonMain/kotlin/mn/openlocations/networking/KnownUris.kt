package mn.openlocations.networking

import mn.openlocations.domain.models.Location
import mn.openlocations.domain.models.Url
import mn.openlocations.domain.models.build
import mn.openlocations.domain.models.toPortableUrl

object KnownUris {
    val website = "https://waterfinder.org?ref=WaterFinder".toPortableUrl()!!
    val developer = "https://mario.nachbaur.dev?ref=WaterFinder".toPortableUrl()!!
    val mapillary = "https://graph.mapillary.com".toPortableUrl()!!
    val reporting = "https://api.waterfinder.org".toPortableUrl()!!
    val panoramax = "https://api.panoramax.xyz".toPortableUrl()!!
    val geocoding = "https://geocode.maps.co/".toPortableUrl()!!
    val overpass = listOf<Url>(
        "https://overpass.private.coffee/api".toPortableUrl()!!,
        "https://overpass-api.de/api".toPortableUrl()!!,
        "https://maps.mail.ru/osm/tools/overpass/api".toPortableUrl()!!,
        "https://overpass.osm.jp/api".toPortableUrl()!!,
    )
    private val googleMaps = "https://www.google.com/maps".toPortableUrl()!!

    fun fix(location: Location): Url = website.build(FixRoute(location))
    fun googleMaps(location: Location): Url = googleMaps.build(GoogleMapsRoute(location))
}

private class FixRoute(location: Location) : BaseRoute(
    route = "help/fix/",
    parameters = mapOf(
        "lat" to location.latitude,
        "lng" to location.longitude,
    )
)

private class GoogleMapsRoute(location: Location) : BaseRoute(
    route = "search/", // trailing '/' is important
    parameters = mapOf(
        "api" to 1,
        "query" to "${location.latitude},${location.longitude}",
    )
)
