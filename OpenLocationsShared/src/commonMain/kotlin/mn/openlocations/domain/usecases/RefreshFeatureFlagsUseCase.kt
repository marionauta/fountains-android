package mn.openlocations.domain.usecases

import mn.openlocations.domain.repositories.FeatureFlagsRepository
import kotlin.native.ObjCName

object RefreshFeatureFlagsUseCase {
    private val repository = FeatureFlagsRepository

    @ObjCName("callAsFunction")
    suspend operator fun invoke() {
        repository.fetch()
    }
}
