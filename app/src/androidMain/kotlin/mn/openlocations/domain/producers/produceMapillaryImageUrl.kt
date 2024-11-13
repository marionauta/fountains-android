package mn.openlocations.domain.producers

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import mn.openlocations.BuildConfig
import mn.openlocations.domain.usecases.GetMapillaryUrlUseCase

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun produceMapillaryImageUrl(mapillaryId: String?): State<String?> {
    val useCase = GetMapillaryUrlUseCase(mapillaryToken = BuildConfig.MAPILLARY_TOKEN)
    return produceState<String?>(initialValue = null, mapillaryId) {
        value = if (mapillaryId == null) null else useCase(mapillaryId)
    }
}
