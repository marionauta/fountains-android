package mn.openlocations.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationButton(
    permissionState: MultiplePermissionsState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    colors: ButtonColors = ButtonDefaults.elevatedButtonColors(),
    size: Dp = 48.dp,
    contentPadding: PaddingValues = PaddingValues(size / 6),
    shape: Shape = CircleShape,
) {
    val isGranted = permissionState.permissions.any { it.status.isGranted }

    ElevatedButton(
        modifier = modifier
            .requiredSize(size)
            .aspectRatio(1f),
        onClick = {
            if (!isGranted) {
                permissionState.launchMultiplePermissionRequest()
            }
            onClick()
        },
        shape = shape,
        colors = colors,
        contentPadding = contentPadding,
    ) {
        // TODO: use a better icon and content description
        Image(
            contentDescription = null,
            imageVector = Icons.Rounded.LocationOn,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
