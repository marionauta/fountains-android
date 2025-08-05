package mn.openlocations.domain.models

import platform.Foundation.NSURL

actual typealias Url = NSURL

actual fun String.toPortableUrl(): Url? {
    return NSURL(string = this)
}
