package mn.fountains.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FountainPropertiesDto(
    val bottle: String,
    val wheelchair: String,
    val checkDate: String?,
)
