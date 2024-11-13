package mn.openlocations.domain.models

import kotlinx.datetime.Instant

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PortableDate

expect fun Instant.toPortableDate(): PortableDate
