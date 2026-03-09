package mn.openlocations.screens.map

import kotlinx.serialization.Serializable

@Serializable
data class MarkerProperties(
    val id: String,
    val type: String,
    val fee: Boolean,
    val closed: Boolean,
)
