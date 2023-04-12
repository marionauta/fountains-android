package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import mn.openlocations.domain.models.FountainsResponse
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.repositories.FountainRepository
import mn.openlocations.library.debounce
import kotlin.math.sqrt

@Composable
fun produceFountains(bounds: Pair<Location, Location>?): State<FountainsResponse?> {
    val repository = FountainRepository()
    return produceState<FountainsResponse?>(initialValue = null, bounds) {
        if (bounds == null) {
            return@produceState
        }
        val d = calculateDistanceBetweenPoints(
            bounds.first.longitude,
            bounds.first.latitude,
            bounds.second.longitude,
            bounds.second.latitude,
        )
        if (d > 0.06) {
            return@produceState
        }
        val debounced = debounce<Pair<Location, Location>, FountainsResponse?>(
            waitMs = 50,
            coroutineScope = this,
        ) {
            return@debounce repository.inside(it.first, bounds.second)
        }(bounds)
        value = debounced?.await()
    }
}

private fun calculateDistanceBetweenPoints(
    x1: Double,
    y1: Double,
    x2: Double,
    y2: Double,
): Double {
    return sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1))
}
