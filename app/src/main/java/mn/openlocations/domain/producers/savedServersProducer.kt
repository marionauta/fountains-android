package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import mn.openlocations.domain.models.Area
import mn.openlocations.domain.repositories.AreaRepository

@Composable
fun savedServersProducer(): State<Pair<List<Area>, Boolean>> {
    val repository = AreaRepository()
    return produceState(initialValue = Pair(listOf(), true), Unit) {
        val servers = repository.all().sortedBy { it.name }
        value = Pair(servers, false)
    }
}
