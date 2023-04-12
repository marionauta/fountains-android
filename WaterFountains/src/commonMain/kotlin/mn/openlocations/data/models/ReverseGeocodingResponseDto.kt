package mn.openlocations.data.models

import kotlinx.serialization.Serializable

@Serializable
internal data class ReverseGeocodingResponseDto(
    val address: Address? = null
) {
    @Serializable
    data class Address(
        val city: String? = null,
        val town: String? = null,
        val village: String? = null,
    )

    val name: String? = address?.let { it.city ?: it.town ?: it.village }
}
