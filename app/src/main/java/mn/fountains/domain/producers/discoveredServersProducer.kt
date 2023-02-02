package mn.fountains.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import mn.fountains.domain.models.ServerDiscoveryItem
import mn.fountains.domain.repositories.DiscoveryRepository

@Composable
fun discoveredServersProducer(): State<Pair<List<ServerDiscoveryItem>, Boolean>> {
    val repository = DiscoveryRepository()
    return produceState(initialValue = Pair(listOf(), true), Unit) {
        val servers = repository.all()
        value = Pair(servers, false)
    }
}
