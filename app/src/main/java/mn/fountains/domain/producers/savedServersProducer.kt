package mn.fountains.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import mn.fountains.domain.models.Server
import mn.fountains.domain.repositories.ServerRepository

@Composable
fun savedServersProducer(): State<Pair<List<Server>, Boolean>> {
    val context = LocalContext.current
    val repository = ServerRepository(context)
    return produceState(initialValue = Pair(listOf(), true), Unit) {
        val servers = repository.all().sortedBy { it.name }
        value = Pair(servers, false)
    }
}
