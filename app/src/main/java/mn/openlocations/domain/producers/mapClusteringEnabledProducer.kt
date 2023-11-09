package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import mn.openlocations.domain.repositories.PreferencesRepository

@Composable
fun mapClusteringEnabledProducer(): State<Boolean> {
    val context = LocalContext.current
    return produceState(initialValue = true, Unit) {
        val repository = PreferencesRepository(context)
        value = repository.getMapClusteringEnabled()
    }
}
