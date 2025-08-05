package mn.openlocations.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun RowItem(
    title: String,
    titleIcon: ImageVector? = null,
    content: String? = null,
    contentIsFaded: Boolean = true,
    hasTopDivider: Boolean = true,
    trailingContent: (@Composable () -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    Column {
        if (hasTopDivider) {
            Divider()
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(all = 16.dp)
                .fillMaxWidth(),
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (titleIcon != null) {
                        Icon(
                            imageVector = titleIcon,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                        )
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
                if (content != null) {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (contentIsFaded) LocalContentColor.current.copy(alpha = .5f) else LocalContentColor.current
                    )
                }
                if (bottomContent != null) {
                    bottomContent()
                }
            }
            if (trailingContent != null) {
                trailingContent()
            }
        }
    }
}
