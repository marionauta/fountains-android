package mn.openlocations.domain.usecases

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual object GetLanguagesUseCase {
    actual operator fun invoke(): List<String> {
        return listOf(NSLocale.currentLocale.languageCode)
    }
}
