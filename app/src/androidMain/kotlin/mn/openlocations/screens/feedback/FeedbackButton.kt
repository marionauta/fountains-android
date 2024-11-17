package mn.openlocations.screens.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
            .background(MaterialTheme.colors.surface)
            .border(
                4.dp,
                if (selected) MaterialTheme.colors.secondary else Color.Transparent,
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
