package mn.openlocations.domain.models

actual typealias PortableUrl = String

actual fun String.toPortableUrl(): PortableUrl? = this
