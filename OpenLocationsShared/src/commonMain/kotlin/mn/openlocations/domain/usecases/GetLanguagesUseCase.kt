package mn.openlocations.domain.usecases

expect object GetLanguagesUseCase {
    operator fun invoke(): List<String>
}
