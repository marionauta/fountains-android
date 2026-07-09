package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mn.openlocations.domain.repositories.PreferencesRepository
import mn.openlocations.domain.repositories.PreferencesRepository.Companion.DEFAULT_MAX_DISTANCE

@Composable
fun mapMaxDistanceProducer(): State<Float> {
    val repository = PreferencesRepository(LocalContext.current)
    return repository.getMapMaxDistance()
        .collectAsStateWithLifecycle(initialValue = DEFAULT_MAX_DISTANCE)
}
