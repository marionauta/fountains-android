package mn.openlocations.data.routes

import mn.openlocations.networking.BaseRoute

class WikimediaCommonsRoute(name: String, width: Int = 600, height: Int = 600) : BaseRoute(
    route = "api.php",
    parameters = mapOf(
        "action" to "query",
        "titles" to name,
        "prop" to "imageinfo",
        "iilimit" to 1,
        "iiprop" to "url|extmetadata",
        "iiurlwidth" to width,
        "iiurlheight" to height,
        "iiextmetadatafilter" to "Artist|LicenseShortName|LicenseUrl",
        "format" to "json",
    )
)
