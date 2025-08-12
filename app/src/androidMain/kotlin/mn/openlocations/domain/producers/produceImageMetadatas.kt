package mn.openlocations.domain.producers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import mn.openlocations.BuildConfig
import mn.openlocations.domain.models.ImageMetadata
import mn.openlocations.domain.models.ImageSource
import mn.openlocations.domain.usecases.GetImagesUseCase

@Composable
fun produceImageMetadatas(imageIds: List<Pair<ImageSource, String>>): State<List<ImageMetadata>> {
    val getImages = GetImagesUseCase(mapillaryToken = BuildConfig.MAPILLARY_TOKEN)
    return produceState<List<ImageMetadata>>(initialValue = emptyList(), imageIds) {
        value = getImages(imageIds)
    }
}
