package mn.openlocations.domain.models

import kotlinx.datetime.Instant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDate

actual typealias PortableDate = NSDate

actual fun Instant.toPortableDate(): PortableDate {
    return toNSDate()
}
