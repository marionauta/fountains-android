package mn.openlocations.domain.models

import platform.Foundation.NSURL

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias PortableUrl = NSURL

actual fun String.toPortableUrl(): PortableUrl? {
    return NSURL(string = this)
}
