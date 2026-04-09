package mn.openlocations.domain.models

import mn.openlocations.networking.KnownUris

sealed interface FeatureFlag<Value> {
    val name: String
    val default: Value

    object OverpassHosts : FeatureFlag<List<String>> {
        override val name: String = "overpass_hosts"
        override val default: List<String>
            get() = KnownUris.overpass.map(Url::toString)
    }
}
