package mn.openlocations.networking

import platform.Foundation.NSBundle

actual open class UserAgentMobile {
    actual val agent: String

    actual constructor() {
        val bundle = NSBundle.mainBundle
        val version = bundle.infoDictionary!!["CFBundleShortVersionString"] as String
        val build = bundle.infoDictionary!!["CFBundleVersion"] as String
        agent = "WaterFinder/$version($build) (iPhone; Mobile)"
    }
}
