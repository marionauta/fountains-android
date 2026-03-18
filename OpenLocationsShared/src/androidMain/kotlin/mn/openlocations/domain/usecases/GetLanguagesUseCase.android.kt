package mn.openlocations.domain.usecases

import java.util.Locale

actual object GetLanguagesUseCase {
    actual operator fun invoke(): List<String> {
        return listOf(Locale.getDefault().language)
    }
}
