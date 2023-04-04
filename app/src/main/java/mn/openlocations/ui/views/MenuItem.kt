package mn.openlocations.ui.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun MenuItem(
    imageVector: ImageVector,
    title: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    DropdownMenuItem(onClick = onClick, enabled = enabled) {
        Icon(
            imageVector = imageVector,
            contentDescription = title,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title)
    }
}
