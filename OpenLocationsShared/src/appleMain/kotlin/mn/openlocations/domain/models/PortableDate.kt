package mn.openlocations.domain.models

import kotlinx.datetime.toNSDate
import platform.Foundation.NSDate
import kotlin.time.Instant

actual typealias PortableDate = NSDate

actual fun Instant.toPortableDate(): PortableDate {
    return toNSDate()
}
