package mn.openlocations.ui.helpers

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.MapStyleOptions
import mn.openlocations.R

@Composable
fun mapStyleOptions(): MapStyleOptions {
    return MapStyleOptions.loadRawResourceStyle(
        LocalContext.current,
        if (isSystemInDarkTheme()) R.raw.map_style_dark else R.raw.map_style,
    )
}
