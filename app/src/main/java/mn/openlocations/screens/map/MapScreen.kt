package mn.openlocations.screens.map

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import mn.openlocations.R
import mn.openlocations.domain.models.Area
import mn.openlocations.domain.models.Fountain
import mn.openlocations.domain.models.FountainsResponse
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.repositories.AreaRepository
import mn.openlocations.domain.repositories.FountainRepository
import mn.openlocations.domain.repositories.PreferencesRepository
import mn.openlocations.screens.fountain.FountainDetailScreen
import mn.openlocations.screens.info.AppInfoModal
import mn.openlocations.ui.helpers.mapStyleOptions
import mn.openlocations.ui.theme.Typography
import mn.openlocations.ui.views.BannerAd
import mn.openlocations.ui.views.EmptyFallback
import mn.openlocations.ui.views.MenuItem
import mn.openlocations.ui.views.Modal
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun MapScreen(id: String, navController: NavController) {
    var isMenuShown by remember { mutableStateOf(false) }

    val (area, setArea) = remember { mutableStateOf<Area?>(null) }
    val (fountains, setFountains) = remember { mutableStateOf<FountainsResponse?>(null) }

    var isAppInfoOpen by rememberSaveable { mutableStateOf(false) }

    var selectedFountainId by rememberSaveable { mutableStateOf<String?>(null) }
    fun deselectFountain() {
        selectedFountainId = null
    }

    LaunchedEffect(id) {
        val repository = AreaRepository()
        setArea(repository.get(id = id))
    }

    LaunchedEffect(area) {
        if (area == null) {
            setFountains(null)
            return@LaunchedEffect
        }
        val repository = FountainRepository()
        setFountains(repository.all(area = area))
    }

    val context = LocalContext.current
    fun closeMap() {
        val repository = PreferencesRepository(context)
        repository.setLastAreaId(null)
        navController.popBackStack()
//        navController.replace(AppScreen.AreaList.route)
    }

    var isDeletingArea by rememberSaveable { mutableStateOf(false) }
    fun deleteArea(area: Area?) {
        if (area == null) {
            return
        }
        isDeletingArea = true
        val repository = AreaRepository()
        runBlocking {
            repository.delete(areaId = area.id)
        }
        closeMap()
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = area?.trimmedDisplayName
                            ?: stringResource(R.string.map_fallback_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    fountains?.lastUpdated?.readable?.let {
                        Text(
                            text = stringResource(R.string.map_last_updated).format(it),
                            style = Typography.caption,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = { closeMap() }) {
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
                    MenuItem(
                        imageVector = Icons.Rounded.Star,
                        title = stringResource(R.string.app_info_menu_item),
                    ) {
                        isAppInfoOpen = true
                    }
                    MenuItem(
                        imageVector = Icons.Rounded.Delete,
                        title = stringResource(R.string.map_delete_server),
                        enabled = !isDeletingArea,
                    ) {
                        deleteArea(area)
                    }
                }
            },
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            if (area == null) {
                NoServer()
            } else {
                Column {
                    BannerAd()
                    Map(
                        location = area.location,
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
            AppInfoModal(
                isOpen = isAppInfoOpen,
                onClose = { isAppInfoOpen = false },
            )
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
            mapStyleOptions = mapStyleOptions(),
        ),
    ) {
        val context = LocalContext.current
        val fountainIcon = bitmapDescriptorFromVector(context, vectorResId = R.drawable.marker)

        MapEffect(Unit) { map ->
            map.setOnCameraIdleListener {
                bounds = map.projection.visibleRegion.latLngBounds
            }
        }
        for (fountain in shownFountains) {
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
