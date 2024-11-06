package mn.openlocations.library

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import mn.openlocations.R

@Composable
fun parsePropertyValue(value: String): String {
    return when (value) {
        "NO" -> stringResource(R.string.property_value_no)
        "LIMITED" -> stringResource(R.string.property_value_limited)
        "YES" -> stringResource(R.string.property_value_yes)
        "UNKNOWN" -> stringResource(R.string.property_value_unknown)
        else -> value
    }
}
