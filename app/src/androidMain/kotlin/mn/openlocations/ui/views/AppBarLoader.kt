package mn.openlocations.ui.views

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppBarLoader(isLoading: Boolean, modifier: Modifier = Modifier) {
    if (isLoading) {
        CircularProgressIndicator(
            color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
            modifier = modifier.size(20.dp),
        )
    }
}
