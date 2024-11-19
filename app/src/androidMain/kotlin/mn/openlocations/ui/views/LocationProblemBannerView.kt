package mn.openlocations.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import mn.openlocations.R
import mn.openlocations.screens.map.LocationProblem

@Composable
fun LocationProblemBannerView(locationProblem: LocationProblem) {
    if (locationProblem == LocationProblem.None) {
        return
    }
    Text(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .padding(all = 16.dp),
        text = stringResource(
            when (locationProblem) {
                LocationProblem.LocationIsOff -> R.string.map_location_disabled
                LocationProblem.PermissionNeeded -> R.string.map_location_permission_denied
                LocationProblem.None -> TODO() // unreachable
            }
        ),
        color = Color.White,
    )
}
