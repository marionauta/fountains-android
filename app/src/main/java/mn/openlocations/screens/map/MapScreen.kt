package mn.openlocations.screens.map

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
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
import mn.openlocations.navigation.AppScreen
import mn.openlocations.ui.theme.Typography
import mn.openlocations.ui.views.BannerAd
import mn.openlocations.ui.views.EmptyFallback
import java.net.URL

@Composable
fun MapScreen(url: URL, navController: NavController) {
    var (showMenu, setShowMenu) = remember { mutableStateOf(false) }
    val (server, setServer) = remember { mutableStateOf<Server?>(null) }
    val (fountains, setFountains) = remember { mutableStateOf<FountainsResponse?>(null) }

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
                    Text(server?.name ?: stringResource(R.string.map_fallback_title))
                    fountains?.lastUpdated?.let {
                        val local = it.toLocalDateTime(TimeZone.currentSystemDefault())
                        Text(
                            stringResource(R.string.map_last_updated).format(local),
                            style = Typography.caption,
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
                IconButton(onClick = { setShowMenu(true) }) {
                    Icon(
                        Icons.Rounded.MoreVert,
                        contentDescription = stringResource(R.string.map_more_menu)
                    )
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { setShowMenu(false) }) {
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
                        onMarkerClick = { fountain ->
                            navController.navigate(AppScreen.FountainDetail.route + "/${fountain.id}")
                        },
                    )
                }
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

@OptIn(ExperimentalPermissionsApi::class)
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

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location.position, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isBuildingEnabled = false,
            isIndoorEnabled = false,
            isTrafficEnabled = false,
            isMyLocationEnabled = isMyLocationEnabled,
        ),
    ) {
        for (fountain in fountains) {
            Marker(state = MarkerState(position = fountain.location.position),
                title = fountain.name,
                onClick = {
                    onMarkerClick(fountain)
                    return@Marker true
                })
        }
    }
}

private val Location.position: LatLng
    get() = LatLng(latitude, longitude)
