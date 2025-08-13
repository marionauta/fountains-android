package mn.openlocations.domain.models

import com.eygraber.uri.Builder
import com.eygraber.uri.Uri
import com.eygraber.uri.toNSURL
import com.eygraber.uri.toUri
import platform.Foundation.NSURL

actual typealias Url = NSURL

actual fun Url.builder(): Builder {
    return toUri()!!.buildUpon()
}

actual fun String.toPortableUrl(): Url? {
    return NSURL.URLWithString(this)
}

actual fun Uri.toPortableUrl(): Url {
    return toNSURL()!!
}
