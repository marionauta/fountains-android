package mn.openlocations.data.routes

import mn.openlocations.networking.BaseRoute
import kotlin.time.Duration.Companion.seconds

object FeatureFlagRoute : BaseRoute(
    route = "v1/flags",
    timeout = 2.seconds,
)
