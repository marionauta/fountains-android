package mn.openlocations.screens.amenity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AmenityPropertyCell(
    title: String,
    subtitle: String? = null,
    image: @Composable (modifier: Modifier) -> Unit,
    badge: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .heightIn(min = 120.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.surface)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp),
        ) {
            Box(
                contentAlignment = Alignment.BottomStart,
                modifier = Modifier.padding(bottom = 4.dp),
            ) {
                image(Modifier.size(50.dp))
                badge()
            }

            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
