package mn.openlocations.screens.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mn.openlocations.domain.models.FeedbackState

@Composable
fun FeedbackButton(
    modifier: Modifier = Modifier,
    variant: FeedbackState,
    selected: Boolean,
    onClick: (state: FeedbackState) -> Unit
) {
    TextButton(
        onClick = { onClick(variant) },
        modifier = modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                4.dp,
                if (selected) MaterialTheme.colorScheme.secondary else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = when (variant) {
                FeedbackState.Good -> "👍"
                FeedbackState.Bad -> "👎"
            },
            fontSize = 30.sp
        )
    }
}
