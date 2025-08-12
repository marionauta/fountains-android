package mn.openlocations.domain.models

import com.eygraber.uri.Builder
import com.eygraber.uri.Uri
import mn.openlocations.networking.ApiRoute

typealias Url = Uri

fun String.toPortableUrl(): Uri? {
    return Uri.parseOrNull(this)
}

fun Url.build(route: ApiRoute): Url {
    return buildUpon()
        .appendEncodedPath(route.route)
        .appendQueryParameters(route.parameters)
        .build()
}

fun Builder.appendQueryParameters(parameters: Map<String, Any>): Builder {
    for ((key, value) in parameters) {
        appendQueryParameter(key, value.toString())
    }
    return this
}
