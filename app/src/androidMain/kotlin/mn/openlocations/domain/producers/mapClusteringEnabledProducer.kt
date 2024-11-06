package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mn.openlocations.domain.repositories.PreferencesRepository

@Composable
fun mapClusteringEnabledProducer(): State<Boolean> {
    val repository = PreferencesRepository(LocalContext.current)
    return repository.getMapClusteringEnabled().collectAsStateWithLifecycle(initialValue = false)
}
