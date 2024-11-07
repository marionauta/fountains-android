package mn.openlocations.domain.models

import kotlinx.datetime.Instant

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias PortableDate = Instant

actual fun Instant.toPortableDate(): PortableDate {
    return this
}