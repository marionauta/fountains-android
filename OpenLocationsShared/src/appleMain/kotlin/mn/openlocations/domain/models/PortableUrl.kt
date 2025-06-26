package mn.openlocations.domain.models

import platform.Foundation.NSURL

actual typealias PortableUrl = NSURL

actual fun String.toPortableUrl(): PortableUrl? {
    return NSURL(string = this)
}
