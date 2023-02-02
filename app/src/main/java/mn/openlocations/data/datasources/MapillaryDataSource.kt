package mn.openlocations.data.datasources

import mn.openlocations.data.models.MapillaryResponseDto
import mn.openlocations.networking.ApiClient
import mn.openlocations.BuildConfig
import java.net.URL

class MapillaryDataSource {
    companion object {
        private val baseUrl = URL("https://graph.mapillary.com")
        private var token = BuildConfig.MAPILLARY_TOKEN
    }

    private val apiClient = ApiClient(baseUrl = baseUrl)

    suspend fun getImage(id: String): String? {
        if (token.isBlank()) { return null }
        val url = "$id?fields=thumb_1024_url&access_token=$token"
        val response = apiClient.get<MapillaryResponseDto>(url)
        return response?.thumb_1024_url
    }
}
