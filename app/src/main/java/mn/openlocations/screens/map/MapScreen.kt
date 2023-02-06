package mn.openlocations.screens.map

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mn.openlocations.R
import mn.openlocations.domain.models.Fountain
import mn.openlocations.domain.models.FountainsResponse
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.models.Server
import mn.openlocations.domain.repositories.FountainRepository
import mn.openlocations.domain.repositories.ServerRepository
import mn.openlocations.screens.fountain.FountainDetailScreen
import mn.openlocations.ui.theme.Typography
import mn.openlocations.ui.views.BannerAd
import mn.openlocations.ui.views.EmptyFallback
import mn.openlocations.ui.views.Modal
import java.net.URL

@Composable
fun MapScreen(url: URL, navController: NavController) {
    var isMenuShown by remember { mutableStateOf(false) }

    val (server, setServer) = remember { mutableStateOf<Server?>(null) }
    val (fountains, setFountains) = remember { mutableStateOf<FountainsResponse?>(null) }

    var selectedFountainId by rememberSaveable { mutableStateOf<String?>(null) }
    fun deselectFountain() {
        selectedFountainId = null
    }

    val context = LocalContext.current
    LaunchedEffect(url) {
        val repository = ServerRepository(context)
        setServer(repository.get(address = url))
    }

    LaunchedEffect(server) {
        if (server == null) {
            setFountains(null)
            return@LaunchedEffect
        }
        val repository = FountainRepository()
        setFountains(repository.all(server))
    }

    fun deleteServer(server: Server?) {
        if (server == null) {
            return
        }
        val repository = ServerRepository(context)
        runBlocking {
            repository.delete(server)
        }
        navController.popBackStack()
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = server?.name ?: stringResource(R.string.map_fallback_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    fountains?.lastUpdated?.let {
                        val local = it.toLocalDateTime(TimeZone.currentSystemDefault())
                        Text(
                            text = stringResource(R.string.map_last_updated).format(local),
                            style = Typography.caption,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(navController::navigateUp) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.general_back),
                    )
                }
            },
            actions = {
                IconButton(onClick = { isMenuShown = true }) {
                    Icon(
                        Icons.Rounded.MoreVert,
                        contentDescription = stringResource(R.string.map_more_menu)
                    )
                }
                DropdownMenu(expanded = isMenuShown, onDismissRequest = { isMenuShown = false }) {
                    val title = stringResource(R.string.map_delete_server)
                    DropdownMenuItem(onClick = { deleteServer(server) }) {
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = title,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = title)
                    }
                }
            },
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            if (server == null) {
                NoServer()
            } else {
                Column {
                    BannerAd()
                    Map(
                        location = server.location,
                        fountains = fountains?.fountains ?: emptyList(),
                        onMarkerClick = { fountain -> selectedFountainId = fountain.id },
                    )
                }
            }
            Modal(
                isOpen = selectedFountainId != null,
                onClose = ::deselectFountain,
            ) {
                FountainDetailScreen(
                    fountainId = selectedFountainId,
                    onClose = ::deselectFountain,
                )
            }
        }
    }
}

@Composable
private fun NoServer() {
    EmptyFallback(
        title = stringResource(R.string.map_not_found_title),
        description = stringResource(R.string.map_not_found_description),
        modifier = Modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalPermissionsApi::class, MapsComposeExperimentalApi::class)
@Composable
private fun Map(location: Location, fountains: List<Fountain>, onMarkerClick: (Fountain) -> Unit) {
    val fineLocationPermission =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val coarseLocationPermission =
        rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    val isMyLocationEnabled =
        fineLocationPermission.status.isGranted || coarseLocationPermission.status.isGranted
    LaunchedEffect(isMyLocationEnabled) {
        if (isMyLocationEnabled) {
            return@LaunchedEffect
        }
        fineLocationPermission.launchPermissionRequest()
    }

    val zoomLevel = 15f
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location.position, zoomLevel)
    }

    var bounds by remember { mutableStateOf<LatLngBounds?>(null) }
    val shownFountains = fountains.filter { bounds?.contains(it.location.position) ?: false }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isBuildingEnabled = false,
            isIndoorEnabled = false,
            isTrafficEnabled = false,
            minZoomPreference = zoomLevel,
            isMyLocationEnabled = isMyLocationEnabled,
        ),
    ) {
        MapEffect(Unit) { map ->
            map.setOnCameraIdleListener {
                bounds = map.projection.visibleRegion.latLngBounds
            }
        }
        for (fountain in shownFountains) {
            Marker(
                state = MarkerState(position = fountain.location.position),
                title = fountain.name,
                onClick = {
                    onMarkerClick(fountain)
                    return@Marker true
                },
            )
        }
    }
}

private val Location.position: LatLng
    get() = LatLng(latitude, longitude)
