package mn.openlocations.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
    onClick: () -> Unit,
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
                    style = MaterialTheme.typography.subtitle1,
                )
            }
            if (content != null) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.caption,
                    color = if (contentIsFaded) LocalContentColor.current.copy(alpha = .5f) else LocalContentColor.current
                )
            }
        }
    }
}
