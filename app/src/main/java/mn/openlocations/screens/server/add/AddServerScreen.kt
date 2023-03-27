package mn.openlocations.screens.server.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.launch
import mn.openlocations.R
import mn.openlocations.data.datasources.NominatimDataSource
import mn.openlocations.data.models.AreaOsm
import mn.openlocations.domain.models.Area
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.repositories.AreaRepository
import mn.openlocations.ui.helpers.mapStyleOptions
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.RowItem
import java.util.*

@Composable
fun AddServerScreen(navController: NavController) {
    var isLoading by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.servers_add_title)) },
            navigationIcon = {
                IconButton(onClick = navController::navigateUp) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.general_close),
                    )
                }
            },
            actions = {
                AppBarLoader(isLoading = isLoading, modifier = Modifier.padding(end = 16.dp))
            }
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            AddServer(
                navController = navController,
                setIsLoading = { value -> isLoading = value }
            )
        }
    }
}

@Composable
fun AddServer(navController: NavController, setIsLoading: (Boolean) -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    // Areas
    var selectedArea by remember { mutableStateOf<Area?>(null) }
    val (areas, setAreas) = remember { mutableStateOf<List<AreaOsm>>(emptyList()) }
    var isLoadingSelectedArea by rememberSaveable { mutableStateOf(false) }
    fun search(query: String) {
        isLoadingSelectedArea = true
        val dataSource = NominatimDataSource()
        coroutineScope.launch {
            setAreas(dataSource.search(query) ?: emptyList())
            isLoadingSelectedArea = false
        }
    }

    LaunchedEffect(isLoadingSelectedArea) {
        setIsLoading(isLoadingSelectedArea)
    }

    // Address Text Field
    var address by rememberSaveable { mutableStateOf("") }

    fun saveSelectedArea(selectedArea: Area?) {
        if (selectedArea == null) {
            return
        }
        val repository = AreaRepository()
        coroutineScope.launch {
            repository.add(selectedArea)
        }
        navController.popBackStack()
    }

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = address,
            onValueChange = { address = it },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                imeAction = ImeAction.Go,
            ),
            keyboardActions = KeyboardActions(
                onGo = { search(query = address) }
            ),
            singleLine = true,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = stringResource(R.string.servers_add_address_placeholder))
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = { search(query = address) },
                enabled = address.isNotBlank(),
            ) {
                Text("Search")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { saveSelectedArea(selectedArea) },
                enabled = selectedArea != null,
            ) {
                Text(stringResource(R.string.servers_add_addButton))
            }
        }

        if (selectedArea != null) {
            Text(
                text = selectedArea!!.name,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            )
            PreviewMap(selectedArea = selectedArea!!)
        } else {
            AreasList(areas = areas) { area ->
                val areaId = area.areaId() ?: return@AreasList
                selectedArea = Area(
                    id = UUID.randomUUID().toString(),
                    name = area.display_name,
                    location = Location(
                        latitude = area.lat.toDouble(),
                        longitude = area.lon.toDouble(),
                    ),
                    osmAreaId = areaId,
                )
                isLoadingSelectedArea = false
            }
        }
    }
}

@Composable
fun AreasList(
    areas: List<AreaOsm>,
    checkDiscoveryItem: (AreaOsm) -> Unit,
) {
    Text(
        text = stringResource(R.string.servers_add_known_servers),
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
    )
    LazyColumn {
        itemsIndexed(areas, key = { _, item -> item.osm_id }) { index, area ->
            RowItem(
                title = area.display_name,
                content = "${area.osm_id}, ${area.osm_type}",
                hasTopDivider = index > 0,
                onClick = { checkDiscoveryItem(area) }
            )
        }
    }
}

@Composable
private fun PreviewMap(selectedArea: Area) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
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
