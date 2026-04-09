package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.coroutines.delay
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.repositories.GeocodingRepository
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun produceLocationName(coordinate: Location?): State<String?> {
    val repository = GeocodingRepository()
    return produceState(initialValue = null, coordinate) {
        if (coordinate == null) {
            return@produceState
        }
        delay(5_000.milliseconds)
        value = repository.reverse(coordinate)
    }
}
