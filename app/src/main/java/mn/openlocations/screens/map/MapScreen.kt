package mn.openlocations.screens.map

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.*
import com.google.maps.android.compose.clustering.Clustering
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import mn.openlocations.R
import mn.openlocations.domain.models.Fountain
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.producers.produceFountains
import mn.openlocations.domain.producers.produceLocationName
import mn.openlocations.screens.fountain.FountainDetailScreen
import mn.openlocations.screens.info.AppInfoModal
import mn.openlocations.ui.helpers.mapStyleOptions
import mn.openlocations.ui.theme.ColorPrimary
import mn.openlocations.ui.theme.Typography
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.BannerView
import mn.openlocations.ui.views.MenuItem
import mn.openlocations.ui.views.Modal
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun MapScreen() {
    var isMenuShown by rememberSaveable { mutableStateOf(false) }
    var isAppInfoOpen by rememberSaveable { mutableStateOf(false) }

    val (bounds, setBounds) = remember { mutableStateOf<LatLngBounds?>(null) }
    val locationName by produceLocationName(coordinate = bounds?.center?.location)
    val fountainsResult by produceFountains(bounds = bounds?.domain)
    val isLoadingFountains = fountainsResult.isLoading
    val fountains = fountainsResult.response

    var selectedFountainId by rememberSaveable { mutableStateOf<String?>(null) }
    fun deselectFountain() {
        selectedFountainId = null
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = locationName ?: stringResource(R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    fountains?.lastUpdated?.readable?.let {
                        Text(
                            text = it,
                            style = Typography.caption,
                            color = LocalContentColor.current.copy(alpha = .8f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            },
            actions = {
                AppBarLoader(
                    isLoading = isLoadingFountains,
                    modifier = Modifier.padding(end = 16.dp),
                )
                IconButton(onClick = { isMenuShown = true }) {
                    Icon(
                        Icons.Rounded.MoreVert,
                        contentDescription = stringResource(R.string.map_more_menu)
                    )
                }
                DropdownMenu(expanded = isMenuShown, onDismissRequest = { isMenuShown = false }) {
                    MenuItem(
                        imageVector = Icons.Rounded.Star,
                        title = stringResource(R.string.app_info_menu_item),
                    ) {
                        isAppInfoOpen = true
                    }
                }
            },
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            Column {
                BannerView()
                Map(
                    fountains = fountains?.fountains ?: emptyList(),
                    setBounds = setBounds,
                    onMarkerClick = { fountain -> selectedFountainId = fountain.id },
                )
            }
            if (fountainsResult.tooFarAway) {
                Text(
                    text = stringResource(R.string.map_too_far_away),
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = 100.dp)
                        .background(Color.Black.copy(alpha = .75f))
                        .padding(12.dp),
                )
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
            AppInfoModal(
                isOpen = isAppInfoOpen,
                onClose = {
                    isMenuShown = false
                    isAppInfoOpen = false
                },
            )
        }
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class, MapsComposeExperimentalApi::class)
@Composable
private fun Map(
    fountains: List<Fountain>,
    setBounds: (LatLngBounds) -> Unit,
    onMarkerClick: (Fountain) -> Unit,
) {
    val fineLocationPermission =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val coarseLocationPermission =
        rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    val isMyLocationEnabled =
        fineLocationPermission.status.isGranted || coarseLocationPermission.status.isGranted

    val zoomLevel = 15f
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(.0, .0), zoomLevel)
    }

    val context = LocalContext.current
    LaunchedEffect(isMyLocationEnabled) {
        if (isMyLocationEnabled) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location == null) {
                    return@addOnSuccessListener
                }
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(location.latitude, location.longitude),
                    zoomLevel,
                )
            }
        } else {
            fineLocationPermission.launchPermissionRequest()
        }
    }

    val fountainIcon = painterResource(id = R.drawable.marker)
    var clusterItems by rememberSaveable { mutableStateOf<List<FountainClusterItem>>(emptyList()) }
    LaunchedEffect(fountains) {
        clusterItems = fountains.map { FountainClusterItem(it) }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isBuildingEnabled = false,
            isIndoorEnabled = false,
            isTrafficEnabled = false,
            isMyLocationEnabled = isMyLocationEnabled,
            mapStyleOptions = mapStyleOptions(),
        ),
    ) {
        MapEffect(Unit) { map ->
            map.setOnCameraIdleListener {
                setBounds(map.projection.visibleRegion.latLngBounds)
            }
        }
        Clustering(
            items = clusterItems,
            onClusterClick = { true }, // Do nothing
            onClusterItemClick = {
                onMarkerClick(it.fountain)
                return@Clustering true
            },
            clusterContent = { cluster ->
                ClusterContent(cluster = cluster)
            },
            clusterItemContent = {
                Image(fountainIcon, it.title)
            },
        )
    }
}

@Composable
private fun <T : ClusterItem> ClusterContent(cluster: Cluster<T>) {
    Surface(
        Modifier.size(30.dp),
        shape = CircleShape,
        color = ColorPrimary,
        contentColor = Color.White,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "%,d".format(cluster.size),
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private class FountainClusterItem(val fountain: Fountain) : ClusterItem {
    override fun getPosition(): LatLng {
        return fountain.location.position
    }

    override fun getTitle(): String {
        return fountain.name
    }

    override fun getSnippet(): String? {
        return null
    }

    override fun getZIndex(): Float? {
        return null
    }
}

private val Location.position: LatLng
    get() = LatLng(latitude, longitude)

private val LatLng.location: Location
    get() = Location(latitude, longitude)

private val LatLngBounds.domain: Pair<Location, Location>
    get() = northeast.location to southwest.location

private val Instant.readable: String
    get() {
        val dateTime = toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        return dateTime.format(formatter)
    }
