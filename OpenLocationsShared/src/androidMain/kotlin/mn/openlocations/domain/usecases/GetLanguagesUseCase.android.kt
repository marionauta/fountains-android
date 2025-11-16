package mn.openlocations.domain.usecases

import java.util.Locale

actual object GetLanguagesUseCase {
    actual fun get(): List<String> {
        return listOf(Locale.getDefault().language)
    }
}
