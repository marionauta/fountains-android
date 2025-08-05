package mn.openlocations.screens.amenity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import mn.openlocations.R

enum class Variant {
    Positive,
    Negative,
    Limited,
    Unknown,
}

@Composable
fun AmenityPropertyBadge(variant: Variant) {
    Surface(
        Modifier.size(22.dp),
        shape = CircleShape,
        color = colorResource(
            when (variant) {
                Variant.Negative -> R.color.property_negative
                Variant.Limited -> R.color.property_limited
                Variant.Positive -> R.color.property_positive
                Variant.Unknown -> R.color.property_unknown
            }
        ),
        contentColor = MaterialTheme.colorScheme.onPrimary,
        border = BorderStroke(2.dp, Color.White),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(
                    when (variant) {
                        Variant.Negative -> R.drawable.variant_negative
                        Variant.Limited -> R.drawable.variant_limited
                        Variant.Positive -> R.drawable.variant_positive
                        Variant.Unknown -> R.drawable.variant_unknown
                    }
                ),
                contentDescription = stringResource(
                    when (variant) {
                        Variant.Negative -> R.string.property_value_no
                        Variant.Limited -> R.string.property_value_limited
                        Variant.Positive -> R.string.property_value_yes
                        Variant.Unknown -> R.string.property_value_unknown
                    }
                ),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier.size(14.dp),
            )
        }
    }
}
