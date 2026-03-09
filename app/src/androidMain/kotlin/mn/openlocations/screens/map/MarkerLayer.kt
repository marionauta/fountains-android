package mn.openlocations.screens.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.JsonPrimitive
import mn.openlocations.R
import org.maplibre.compose.expressions.dsl.asBoolean
import org.maplibre.compose.expressions.dsl.asString
import org.maplibre.compose.expressions.dsl.case
import org.maplibre.compose.expressions.dsl.condition
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.feature
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.dsl.switch
import org.maplibre.compose.expressions.value.SymbolAnchor
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.Source
import org.maplibre.compose.util.ClickResult

// TODO: handle clustering
@Composable
fun MarkerLayer(
    source: Source,
    onMarkerClick: (String) -> Unit,
) {
    SymbolLayer(
        id = "mn.openlocations.amenities",
        source = source,
        iconImage = switch(
            input = feature["type"].asString(),
            case(
                "fountain", switch(
                    condition(
                        test = feature["closed"].asBoolean(),
                        output = image(painterResource(R.drawable.marker_fountain_inactive)),
                    ),
                    fallback = image(painterResource(R.drawable.marker_fountain)),
                )
            ),
            case(
                "restroom", switch(
                    condition(
                        test = feature["closed"].asBoolean(),
                        output = image(painterResource(R.drawable.marker_restroom_inactive)),
                    ),
                    fallback = image(painterResource(R.drawable.marker_restroom)),
                )
            ),
            fallback = image(painterResource(R.drawable.marker_restroom)),
        ),
        iconAnchor = const(SymbolAnchor.Center),
        iconAllowOverlap = const(true),
        iconIgnorePlacement = const(true),
        onClick = { features ->
            val id = features.firstOrNull()?.properties?.get("id")
            if (id != null && id is JsonPrimitive && id.content.isNotBlank()) {
                onMarkerClick(id.content)
                return@SymbolLayer ClickResult.Consume
            }
            return@SymbolLayer ClickResult.Pass
        }
    )
    SymbolLayer(
        id = "mn.openlocations.amenities.fee",
        source = source,
        filter = feature["fee"].asBoolean(),
        iconImage = image(painterResource(R.drawable.fee_badge)),
        iconAnchor = const(SymbolAnchor.Center),
        iconOffset = const(DpOffset(10.dp, (-10).dp)),
        iconAllowOverlap = const(true),
        iconIgnorePlacement = const(true),
    )
}
