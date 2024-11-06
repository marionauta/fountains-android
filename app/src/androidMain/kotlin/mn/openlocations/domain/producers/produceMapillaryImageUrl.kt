package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import mn.openlocations.BuildConfig
import mn.openlocations.domain.repositories.MapillaryRepository

@Composable
fun produceMapillaryImageUrl(mapillaryId: String?): State<String?> {
    val repository = MapillaryRepository(mapillaryToken = BuildConfig.MAPILLARY_TOKEN)
    return produceState<String?>(initialValue = null, mapillaryId) {
        if (mapillaryId == null) { return@produceState }
        value = repository.getImageUrl(mapillaryId)
    }
}
