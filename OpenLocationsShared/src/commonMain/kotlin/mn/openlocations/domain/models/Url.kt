package mn.openlocations.domain.models

import com.eygraber.uri.Builder
import com.eygraber.uri.Uri
import mn.openlocations.networking.ApiRoute

expect class Url

expect fun Url.builder(): Builder
expect fun String.toPortableUrl(): Url?
expect fun Uri.toPortableUrl(): Url

fun Url.build(route: ApiRoute): Url {
    return builder()
        .appendEncodedPath(route.route)
        .appendQueryParameters(route.parameters)
        .build()
        .toPortableUrl()
}

fun Builder.appendQueryParameters(parameters: Map<String, Any>): Builder {
    for ((key, value) in parameters) {
        appendQueryParameter(key, value.toString())
    }
    return this
}
