package mn.fountains.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import mn.fountains.R
import mn.fountains.domain.models.Fountain
import mn.fountains.domain.models.Location
import mn.fountains.domain.models.Server
import mn.fountains.domain.repositories.FountainRepository
import mn.fountains.domain.repositories.ServerRepository
import mn.fountains.navigation.AppScreen
import mn.fountains.ui.views.EmptyFallback
import java.net.URL

@Composable
fun MapScreen(url: URL, navController: NavController) {
    val (server, setServer) = remember { mutableStateOf<Server?>(null) }
    val context = LocalContext.current
    LaunchedEffect(url) {
        val repository = ServerRepository(context)
        setServer(repository.get(address = url))
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(server?.name ?: stringResource(R.string.map_fallback_title)) },
            navigationIcon = {
                IconButton(navController::navigateUp) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.general_back),
                    )
                }
            })
    }) {
        Box(modifier = Modifier.padding(it)) {
            if (server == null) {
                NoServer()
            } else {
                Map(
                    server = server,
                    onMarkerClick = { fountain ->
                        println(fountain)
                        navController.navigate(AppScreen.FountainDetail.route + "/${fountain.id}")
                    },
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

@Composable
private fun Map(server: Server, onMarkerClick: (Fountain) -> Unit) {
    val (fountains, setFountains) = remember { mutableStateOf(listOf<Fountain>()) }

    LaunchedEffect(server) {
        val repository = FountainRepository()
        setFountains(repository.all(server = server) ?: listOf())
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(server.location.position, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isBuildingEnabled = false,
            isIndoorEnabled = false,
            isTrafficEnabled = false,
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
