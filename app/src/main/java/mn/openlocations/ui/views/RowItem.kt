package mn.openlocations.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RowItem(
    title: String,
    content: String,
    contentIsFaded: Boolean = true,
    hasTopDivider: Boolean = true,
    onClick: () -> Unit
) {
    Column {
        if (hasTopDivider) {
            Divider()
        }
        Column(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(all = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
            )
            Text(
                text = content,
                style = MaterialTheme.typography.caption,
                color = if (contentIsFaded) LocalContentColor.current.copy(alpha = .5f) else LocalContentColor.current
            )
        }
    }
}
