package mn.openlocations.screens.map

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import mn.openlocations.R
import mn.openlocations.domain.models.Fountain
import mn.openlocations.domain.models.FountainsResponse
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.repositories.FountainRepository
import mn.openlocations.screens.fountain.FountainDetailScreen
import mn.openlocations.screens.info.AppInfoModal
import mn.openlocations.ui.helpers.mapStyleOptions
import mn.openlocations.ui.theme.Typography
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.BannerAd
import mn.openlocations.ui.views.MenuItem
import mn.openlocations.ui.views.Modal
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.sqrt

@Composable
fun MapScreen() {
    var isMenuShown by rememberSaveable { mutableStateOf(false) }
    var isAppInfoOpen by rememberSaveable { mutableStateOf(false) }

    val (bounds, setBounds) = remember { mutableStateOf<LatLngBounds?>(null) }
    var isLoadingFountains by rememberSaveable { mutableStateOf(false) }
    val (fountains, setFountains) = remember { mutableStateOf<FountainsResponse?>(null) }

    var selectedFountainId by rememberSaveable { mutableStateOf<String?>(null) }
    fun deselectFountain() {
        selectedFountainId = null
    }

    LaunchedEffect(bounds) {
        if (bounds == null) {
            return@LaunchedEffect
        }
        val d = calculateDistanceBetweenPoints(
            bounds.northeast.longitude,
            bounds.northeast.latitude,
            bounds.southwest.longitude,
            bounds.southwest.latitude,
        )
        if (d > 0.06) {
            return@LaunchedEffect
        }
        isLoadingFountains = true
        val repository = FountainRepository()
        setFountains(
            repository.inside(
                northEast = Location(
                    latitude = bounds.northeast.latitude,
                    longitude = bounds.northeast.longitude,
                ),
                southWest = Location(
                    latitude = bounds.southwest.latitude,
                    longitude = bounds.southwest.longitude,
                ),
            )
        )
        isLoadingFountains = false
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = stringResource(R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    fountains?.lastUpdated?.readable?.let {
                        Text(
                            text = stringResource(R.string.map_last_updated).format(it),
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
                BannerAd()
                Map(
                    location = Location(0.0, 0.0),
                    fountains = fountains?.fountains ?: emptyList(),
                    setBounds = setBounds,
                    onMarkerClick = { fountain -> selectedFountainId = fountain.id },
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

@OptIn(ExperimentalPermissionsApi::class, MapsComposeExperimentalApi::class)
@Composable
private fun Map(
    location: Location,
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
        val context = LocalContext.current
        val fountainIcon = bitmapDescriptorFromVector(context, vectorResId = R.drawable.marker)

        MapEffect(Unit) { map ->
            map.setOnCameraIdleListener {
                setBounds(map.projection.visibleRegion.latLngBounds)
            }
        }
        for (fountain in fountains) {
            Marker(
                state = MarkerState(position = fountain.location.position),
                title = fountain.name,
                icon = fountainIcon,
                anchor = Offset(.5f, .5f),
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

private val Instant.readable: String
    get() {
        val dateTime = toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        return dateTime.format(formatter)
    }

private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888,
    )
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

private fun calculateDistanceBetweenPoints(
    x1: Double,
    y1: Double,
    x2: Double,
    y2: Double,
): Double {
    return sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1))
}
