package mn.openlocations.domain.models

import kotlinx.datetime.Instant

expect class PortableDate

expect fun Instant.toPortableDate(): PortableDate
