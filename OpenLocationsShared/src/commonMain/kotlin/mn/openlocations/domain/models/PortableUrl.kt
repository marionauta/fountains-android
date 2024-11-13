package mn.openlocations.domain.models

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PortableUrl

expect fun String.toPortableUrl(): PortableUrl?
