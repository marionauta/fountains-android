package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.repositories.GeocodingRepository
import mn.openlocations.library.debounce

@Composable
fun produceLocationName(coordinate: Location?): State<String?> {
    val repository = GeocodingRepository()
    return produceState<String?>(initialValue = null, coordinate) {
        if (coordinate == null) {
            return@produceState
        }
        val debounced = debounce<Location, String?>(
            waitMs = 5_000,
            coroutineScope = this,
        ) {
            return@debounce repository.reverse(coordinate = it)
        }(coordinate)
        value = debounced?.await()
    }
}
