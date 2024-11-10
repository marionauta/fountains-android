package mn.openlocations.screens.amenity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mn.openlocations.ui.theme.ColorSecondary

enum class Variant {
    Positive,
    Negative,
    Limited,
    Unknown,
}

@Composable
fun AmenityPropertyBadge(variant: Variant) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(ColorSecondary)
            .border(BorderStroke(2.dp, Color.White), CircleShape)
    ) {
        Text(
            text = when (variant) {
                Variant.Negative -> "x"
                Variant.Limited -> "!"
                Variant.Unknown -> "?"
                Variant.Positive -> ""
            },
            color = Color.White,
        )
    }
}
