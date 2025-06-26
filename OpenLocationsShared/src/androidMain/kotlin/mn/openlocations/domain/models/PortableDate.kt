package mn.openlocations.domain.models

import kotlinx.datetime.Instant

actual typealias PortableDate = Instant

actual fun Instant.toPortableDate(): PortableDate {
    return this
}
