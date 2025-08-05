package mn.openlocations.domain.models

actual typealias Url = String

actual fun String.toPortableUrl(): Url? = this
