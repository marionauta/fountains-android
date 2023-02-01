package mn.fountains.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import mn.fountains.domain.repositories.MapillaryRepository

@Composable
fun produceMapillaryImageUrl(mapillaryId: String?): State<String?> {
    val repository = MapillaryRepository()
    return produceState<String?>(initialValue = null, mapillaryId) {
        if (mapillaryId == null) { return@produceState }
        value = repository.getImageUrl(mapillaryId)
    }
}
