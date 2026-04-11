package mn.openlocations.networking

import android.content.Context
import android.os.Build

actual open class UserAgentMobile actual constructor() {
    companion object {
        private var version: String = "0.0.0"
        private var build: Long = 0

        fun initialize(context: Context) {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            info.versionName?.let { version = it }
            build = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info.longVersionCode
            } else {
                info.versionCode.toLong()
            }
        }
    }

    actual val agent: String
        get() = "WaterFinder/$version($build) (Android; Mobile)"
}
