package mn.openlocations.domain.usecases

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual object GetLanguagesUseCase {
    actual fun get(): List<String> {
        return listOf(NSLocale.currentLocale.languageCode)
    }
}
