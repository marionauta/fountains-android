package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import mn.openlocations.domain.models.Server
import mn.openlocations.domain.repositories.ServerRepository

@Composable
fun savedServersProducer(): State<Pair<List<Server>, Boolean>> {
    val repository = ServerRepository()
    return produceState(initialValue = Pair(listOf(), true), Unit) {
        val servers = repository.all().sortedBy { it.name }
        value = Pair(servers, false)
    }
}
