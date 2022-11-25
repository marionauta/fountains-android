package mn.fountains.screens.server.add

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.launch
import mn.fountains.data.datasources.ServerInfoDataSource
import mn.fountains.data.models.ServerInfoDto
import mn.fountains.ui.theme.Typography
import java.net.URL

@Composable
fun AddServerScreen() {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Add server") })
    }) {
        Box(modifier = Modifier.padding(it)) {
            AddServer()
        }
    }
}

@Composable
fun AddServer() {
    val coroutineScope = rememberCoroutineScope()
    val (address, setAddress) = remember { mutableStateOf("") }
    val (serverInfo, setServerInfo) = remember { mutableStateOf<ServerInfoDto?>(null) }

    Column {
        Row(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextField(value = address, onValueChange = setAddress)
            Button(onClick = {
                val url = URL(address)
                val dataSource = ServerInfoDataSource()
                coroutineScope.launch {
                    val info = dataSource.get(url)
                    println(info)
                    setServerInfo(info)
                }
            }) {
                Text(text = "Check")
            }   
        }

        if (serverInfo != null) {
            Text(
                text = serverInfo.area.displayName,
                style = Typography.h5,
                modifier = Modifier
                    .padding(16.dp),
            )

            val position = serverInfo.area.location.let { LatLng(it.latitude, it.longitude) }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = CameraPositionState(position = CameraPosition.fromLatLngZoom(position, 13f)),
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
    }
}