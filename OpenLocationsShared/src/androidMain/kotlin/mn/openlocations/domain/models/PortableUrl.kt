package mn.openlocations.domain.models

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual typealias PortableUrl = String

actual fun String.toPortableUrl(): PortableUrl? = this
