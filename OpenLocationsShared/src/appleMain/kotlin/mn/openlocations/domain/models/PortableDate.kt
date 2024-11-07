package mn.openlocations.domain.models

import kotlinx.datetime.Instant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDate

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias PortableDate = NSDate

actual fun Instant.toPortableDate(): PortableDate {
    return toNSDate()
}
