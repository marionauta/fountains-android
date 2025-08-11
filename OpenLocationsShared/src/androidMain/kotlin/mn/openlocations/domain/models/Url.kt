package mn.openlocations.domain.models

actual typealias Url = String

// TODO: Perform URL validation
actual fun String.toPortableUrl(): Url? = this
