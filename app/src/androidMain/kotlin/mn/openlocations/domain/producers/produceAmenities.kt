package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import kotlinx.coroutines.delay
import mn.openlocations.domain.models.AmenitiesResponse
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.usecases.GetAmenitiesUseCase
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.milliseconds

data class ProduceAmenitiesResult(
    val amenities: AmenitiesResponse? = null,
    val tooFarAway: Boolean = false,
    val isLoading: Boolean = false,
)

@Composable
fun produceAmenities(bounds: Pair<Location, Location>?): State<ProduceAmenitiesResult> {
    val tooFarDistance by mapMaxDistanceProducer()
    val getAmenitiesUseCase = GetAmenitiesUseCase()
    return produceState(initialValue = ProduceAmenitiesResult(isLoading = false), bounds) {
        if (bounds == null) {
            return@produceState
        }

        val d = calculateMetersBetweenPoints(
            lat1 = bounds.first.latitude,
            lng1 = bounds.first.longitude,
            lat2 = bounds.second.latitude,
            lng2 = bounds.second.longitude,
        )
        value = value.copy(tooFarAway = d >= tooFarDistance)
        if (value.tooFarAway) {
            return@produceState
        }

        // debounce amenity network call
        delay(100.milliseconds)

        value = value.copy(isLoading = true)
        getAmenitiesUseCase(bounds.first, bounds.second).collect { response ->
            value = value.copy(amenities = response)
        }
        value = value.copy(isLoading = false)
    }
}

private fun calculateMetersBetweenPoints(
    lat1: Double,
    lng1: Double,
    lat2: Double,
    lng2: Double,
): Double {
    val earthRadius = 6371000.0 // meters
    val dLat = Math.toRadians((lat2 - lat1))
    val dLng = Math.toRadians((lng2 - lng1))
    val a =
        sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(
            dLng / 2
        ) * sin(dLng / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return (earthRadius * c)
}
