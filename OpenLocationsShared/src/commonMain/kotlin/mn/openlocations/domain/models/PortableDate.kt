package mn.openlocations.domain.models

import kotlin.time.Instant

expect class PortableDate

expect fun Instant.toPortableDate(): PortableDate
