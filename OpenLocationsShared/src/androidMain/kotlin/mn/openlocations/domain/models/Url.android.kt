package mn.openlocations.domain.models

import com.eygraber.uri.Builder
import com.eygraber.uri.Uri

actual typealias Url = String

actual fun Url.builder(): Builder {
    return Uri.parse(this).buildUpon()
}

actual fun String.toPortableUrl(): Url? {
    return this
}

actual fun Uri.toPortableUrl(): Url {
    return toString()
}
