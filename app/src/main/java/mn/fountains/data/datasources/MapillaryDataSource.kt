package mn.fountains.data.datasources

import mn.fountains.BuildConfig
import mn.fountains.data.models.MapillaryResponseDto
import mn.fountains.networking.ApiClient
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
