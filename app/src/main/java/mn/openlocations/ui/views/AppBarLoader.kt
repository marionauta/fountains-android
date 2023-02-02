package mn.openlocations.ui.views

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppBarLoader(isLoading: Boolean, modifier: Modifier = Modifier) {
    if (isLoading) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.onPrimary,
            modifier = modifier.size(20.dp),
        )
    }
}
