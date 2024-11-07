package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import mn.openlocations.domain.models.AmenitiesResponse
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.repositories.FountainRepository
import mn.openlocations.library.debounce
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class ProduceFountainsResult(
    val response: AmenitiesResponse? = null,
    val tooFarAway: Boolean = false,
    val isLoading: Boolean = false,
)

@Composable
fun produceFountains(bounds: Pair<Location, Location>?): State<ProduceFountainsResult> {
    val tooFarDistance by mapMaxDistanceProducer()
    val repository = FountainRepository()
    return produceState(initialValue = ProduceFountainsResult(isLoading = true), bounds) {
        if (bounds == null) {
            return@produceState
        }
        val d = calculateMetersBetweenPoints(
            bounds.first.longitude,
            bounds.first.latitude,
            bounds.second.longitude,
            bounds.second.latitude,
        )
        if (d >= tooFarDistance) {
            value = value.copy(tooFarAway = true)
            return@produceState
        }
        val debounced = debounce<Pair<Location, Location>, ProduceFountainsResult>(
            waitMs = 50,
            coroutineScope = this,
        ) {
            value = value.copy(isLoading = true)
            val response = repository.inside(it.first, bounds.second)
            return@debounce ProduceFountainsResult(response = response)
        }(bounds)
        value = debounced?.await() ?: ProduceFountainsResult()
    }
}

fun calculateMetersBetweenPoints(
    lat1: Double,
    lng1: Double,
    lat2: Double,
    lng2: Double,
): Double {
    val earthRadius = 6371000.0 // meters
    val dLat = Math.toRadians((lat2 - lat1))
    val dLng = Math.toRadians((lng2 - lng1))
    val a = sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLng / 2) * sin(dLng / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return (earthRadius * c)
}
