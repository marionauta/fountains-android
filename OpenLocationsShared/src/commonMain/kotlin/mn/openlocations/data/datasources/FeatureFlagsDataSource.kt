package mn.openlocations.data.datasources

import io.ktor.client.plugins.logging.LogLevel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import mn.openlocations.data.routes.FeatureFlagRoute
import mn.openlocations.networking.ApiClient
import mn.openlocations.networking.KnownUris
import mn.openlocations.networking.UserAgent
import mn.openlocations.utils.ValueMutex

internal object FeatureFlagsDataSource {
    private val apiClient = ApiClient(
        baseUrl = KnownUris.features,
        userAgent = UserAgent.Mobile,
        logLevel = LogLevel.NONE,
    )

    private val features = ValueMutex(mutableMapOf<String, JsonElement>())

    suspend fun fetchFeatureFlags() {
        val result = apiClient.get<FlagsResult>(FeatureFlagRoute) ?: return
        return features.withLock { features ->
            features.clear()
            features.putAll(result.data)
        }
    }

    suspend fun get(name: String): JsonElement? {
        val needsFetch = features.withLock { !it.contains(name) }
        if (needsFetch) fetchFeatureFlags()
        return features.withLock { it[name] }
    }

    @Serializable
    class FlagsResult(
        val data: Map<String, JsonElement>,
    )
}
