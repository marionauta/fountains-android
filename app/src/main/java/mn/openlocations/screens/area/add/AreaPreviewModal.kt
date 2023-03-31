package mn.openlocations.screens.area.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import mn.openlocations.R
import mn.openlocations.domain.models.Area
import mn.openlocations.ui.helpers.mapStyleOptions

@Composable
fun AreaPreviewModal(area: Area, onAddArea: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.background,
    ) {
        Column {
            Text(
                text = area.name,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(16.dp),
            )
            PreviewMap(selectedArea = area)
            Button(onClick = onAddArea, modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.areas_add_add_button))
            }
        }
    }
}

@Composable
private fun PreviewMap(selectedArea: Area) {
    GoogleMap(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth(),
        cameraPositionState = CameraPositionState(
            position = CameraPosition.fromLatLngZoom(
                selectedArea.location.let { LatLng(it.latitude, it.longitude) },
                13f
            )
        ),
        properties = MapProperties(
            mapStyleOptions = mapStyleOptions(),
        ),
        uiSettings = MapUiSettings(
            compassEnabled = false,
            myLocationButtonEnabled = false,
            mapToolbarEnabled = false,
            rotationGesturesEnabled = false,
            scrollGesturesEnabled = false,
            scrollGesturesEnabledDuringRotateOrZoom = false,
            tiltGesturesEnabled = false,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = false,
            indoorLevelPickerEnabled = false,
        )
    )
}
