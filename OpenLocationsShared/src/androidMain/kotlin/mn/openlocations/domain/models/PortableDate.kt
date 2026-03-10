package mn.openlocations.domain.models

import kotlin.time.Instant

actual typealias PortableDate = Instant

actual fun Instant.toPortableDate(): PortableDate {
    return this
}
