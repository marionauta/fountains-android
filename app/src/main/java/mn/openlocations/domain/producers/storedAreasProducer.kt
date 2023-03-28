package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import mn.openlocations.domain.models.Area
import mn.openlocations.domain.repositories.AreaRepository

@Composable
fun storedAreasProducer(): State<Pair<List<Area>, Boolean>> {
    val repository = AreaRepository()
    return produceState(initialValue = Pair(listOf(), true), Unit) {
        repository.allStream().collect { areas ->
            value = Pair(areas.sortedBy { it.name }, false)
        }
    }
}
