package mn.openlocations.domain.repositories

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import mn.openlocations.data.datasources.FeatureFlagsDataSource
import mn.openlocations.domain.models.FeatureFlag

internal object FeatureFlagsRepository {
    private val dataSource = FeatureFlagsDataSource

    suspend fun fetch() {
        dataSource.fetchFeatureFlags()
    }

    suspend inline fun <reified Value> get(flag: FeatureFlag<Value>): Value {
        val json = dataSource.get(flag.name) ?: return flag.default
        return try {
            Json.decodeFromJsonElement<Value>(json)
        } catch (_: Exception) {
            flag.default
        }
    }
}
