package mn.openlocations.domain.usecases

import kotlin.native.ObjCName

expect object GetLanguagesUseCase {
    fun get(): List<String>
}

@ObjCName("callAsFunction")
operator fun GetLanguagesUseCase.invoke(): List<String> = get()
