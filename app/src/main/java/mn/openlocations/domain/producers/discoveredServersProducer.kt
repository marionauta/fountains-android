package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import mn.openlocations.domain.models.ServerDiscoveryItem
import mn.openlocations.domain.repositories.DiscoveryRepository

@Composable
fun discoveredServersProducer(): State<Pair<List<ServerDiscoveryItem>, Boolean>> {
    val repository = DiscoveryRepository()
    return produceState(initialValue = Pair(listOf(), true), Unit) {
        val servers = repository.all().sortedBy { it.name }
        value = Pair(servers, false)
    }
}
