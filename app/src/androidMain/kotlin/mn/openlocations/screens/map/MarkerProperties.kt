package mn.openlocations.screens.map

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class MarkerProperties(
    val id: String,
    val type: String,
    val fee: Boolean,
    val closed: Boolean,
)
