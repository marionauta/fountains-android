package mn.openlocations.screens.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import mn.openlocations.BuildConfig
import mn.openlocations.R
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.Fountain
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.producers.mapClusteringEnabledProducer
import mn.openlocations.domain.producers.produceFountains
import mn.openlocations.domain.producers.produceLocationName
import mn.openlocations.screens.fountain.FountainDetailScreen
import mn.openlocations.screens.info.AppInfoModal
import mn.openlocations.ui.helpers.mapStyleOptions
import mn.openlocations.ui.theme.ColorMarkerFountain
import mn.openlocations.ui.theme.ColorMarkerRestroom
import mn.openlocations.ui.theme.ColorPrimary
import mn.openlocations.ui.theme.Typography
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.BannerView
import mn.openlocations.ui.views.Modal
import mn.openlocations.ui.views.NeedsLocationBannerView
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
    val (needsLocation, setNeedsLocation) = rememberSaveable { mutableStateOf(true) }

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
                IconButton(onClick = { isAppInfoOpen = true }) {
                    Icon(
                        Icons.Rounded.Settings,
                        contentDescription = stringResource(R.string.app_info_title),
                    )
                }
            },
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            Column {
                BannerView(unitId = BuildConfig.ADMOB_MAP_AD_UNIT_ID)
                NeedsLocationBannerView(isLocationEnabled = needsLocation)
                Map(
                    amenities = fountains?.fountains ?: emptyList(),
                    setBounds = setBounds,
                    setNeedsLocation = setNeedsLocation,
                    onMarkerClick = { amenity -> selectedFountainId = amenity.id },
                )
            }
            if (fountainsResult.tooFarAway) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.map_too_far_away),
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .offset(y = 100.dp)
                            .background(Color.Black.copy(alpha = .75f))
                            .padding(12.dp),
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
    amenities: List<Amenity>,
    setBounds: (LatLngBounds) -> Unit,
    setNeedsLocation: (Boolean) -> Unit,
    onMarkerClick: (Amenity) -> Unit,
) {
    val fineLocationPermission =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val locationPermissions = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )
    val isMyLocationEnabled = locationPermissions.permissions.any { it.status.isGranted }
    LaunchedEffect(isMyLocationEnabled, locationPermissions) {
        val fine = isMyLocationEnabled && !locationPermissions.shouldShowRationale
        setNeedsLocation(fine)
    }

    val zoomLevel = 15f
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(.0, .0), 2f)
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

    val clusteringEnabled by mapClusteringEnabledProducer()

    var clusterFountains by remember { mutableStateOf<List<AmenityClusterItem>>(emptyList()) }
    var clusterRestrooms by remember { mutableStateOf<List<AmenityClusterItem>>(emptyList()) }
    LaunchedEffect(amenities) {
        if (!clusteringEnabled) {
            clusterFountains = emptyList()
            clusterRestrooms = emptyList()
            return@LaunchedEffect
        }
        clusterFountains = amenities.filter { it is Fountain }.map { AmenityClusterItem(it) }
        clusterRestrooms =
            amenities.filter { it is Amenity.Restroom }.map { AmenityClusterItem(it) }
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
        if (clusteringEnabled) {
            for (cluster in listOf(clusterFountains, clusterRestrooms)) {
                Clustering(
                    items = cluster,
                    onClusterClick = { true }, // Do nothing
                    onClusterItemClick = {
                        onMarkerClick(it.amenity)
                        return@Clustering true
                    },
                    clusterContent = { cluster ->
                        ClusterContent(cluster = cluster)
                    },
                    clusterItemContent = {
                        when (it.amenity) {
                            is Fountain -> FountainContent(it.amenity)
                            is Amenity.Restroom -> RestroomContent()
                        }
                    },
                )
            }
        } else {
            for (amenity in amenities) {
                MarkerComposable(
                    state = remember { MarkerState(position = amenity.location.position) },
                    title = amenity.name,
                    anchor = Offset(.5f, .5f),
                    onClick = {
                        onMarkerClick(amenity)
                        return@MarkerComposable true
                    },
                ) {
                    when (amenity) {
                        is Fountain -> FountainContent(amenity)
                        is Amenity.Restroom -> RestroomContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun FountainContent(fountain: Fountain) {
    Surface(
        Modifier.size(28.dp),
        shape = CircleShape,
        color = Color.White,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.marker_fountain),
                contentDescription = fountain.name
            )
        }
    }
}

@Composable
private fun RestroomContent() {
    Surface(
        Modifier.size(28.dp),
        shape = CircleShape,
        color = ColorMarkerRestroom,
        contentColor = Color.White,
        border = BorderStroke(1.5.dp, Color.White),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "WC",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ClusterContent(cluster: Cluster<AmenityClusterItem>) {
    Surface(
        Modifier.size(30.dp),
        shape = CircleShape,
        color = when (cluster.items.firstOrNull()?.amenity) {
            is Fountain -> ColorMarkerFountain
            is Amenity.Restroom -> ColorMarkerRestroom
            else -> ColorPrimary
        },
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

private data class AmenityClusterItem(val amenity: Amenity) : ClusterItem {
    override fun getPosition(): LatLng {
        return amenity.location.position
    }

    override fun getTitle(): String {
        return amenity.name
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
