package mn.openlocations.ui.views

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppBarLoader(isLoading: Boolean, modifier: Modifier = Modifier) {
    if (isLoading) {
        CircularProgressIndicator(
            color = LocalContentColor.current,
            modifier = modifier.size(20.dp),
        )
    }
}
