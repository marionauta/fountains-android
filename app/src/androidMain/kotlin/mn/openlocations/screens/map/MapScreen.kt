package mn.openlocations.screens.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastFilteredMap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.MapsComposeExperimentalApi
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import mn.openlocations.BuildConfig
import mn.openlocations.R
import mn.openlocations.domain.models.Amenity
import mn.openlocations.data.models.OsmId
import mn.openlocations.domain.models.FeeValue
import mn.openlocations.domain.models.Location
import mn.openlocations.domain.producers.produceFountains
import mn.openlocations.domain.producers.produceLocationName
import mn.openlocations.screens.amenity.AmenityDetailScreen
import mn.openlocations.screens.info.AppInfoModal
import mn.openlocations.ui.theme.ColorMarkerFountain
import mn.openlocations.ui.theme.ColorMarkerRestroom
import mn.openlocations.ui.theme.customColors
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.BannerView
import mn.openlocations.ui.views.LocationProblemBannerView
import mn.openlocations.ui.views.Modal
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.material3.DisappearingCompassButton
import org.maplibre.compose.material3.DisappearingScaleBar
import org.maplibre.compose.material3.ExpandingAttributionButton
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.spatialk.geojson.BoundingBox
import org.maplibre.spatialk.geojson.Position
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    var isAppInfoOpen by rememberSaveable { mutableStateOf(false) }

    val (bounds, setBounds) = remember { mutableStateOf<BoundingBox?>(null) }
    val locationName by produceLocationName(coordinate = bounds?.center)
    val fountainsResult by produceFountains(bounds = bounds?.domain)
    val isLoadingFountains = fountainsResult.isLoading
    val fountains = fountainsResult.response

    var selectedOsmId by rememberSaveable { mutableStateOf<OsmId?>(null) }
    fun deselectAmenity() {
        selectedOsmId = null
    }

    var locationProblem by rememberSaveable { mutableStateOf(LocationProblem.None) }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.customColors,
            title = {
                Column {
                    Text(
                        text = locationName.orEmpty().ifBlank { stringResource(R.string.app_name) },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    fountains?.lastUpdated?.readableDateTime?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
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
                LocationProblemBannerView(locationProblem = locationProblem)
                Map(
                    amenities = fountains?.amenities ?: emptyList(),
                    setBounds = setBounds,
                    setLocationProblem = { locationProblem = it },
                    onMarkerClick = { amenity -> selectedOsmId = amenity.id },
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
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .offset(y = 100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = .75f))
                            .padding(12.dp),
                    )
                }
            }
            Modal(
                isOpen = selectedOsmId != null,
                onClose = ::deselectAmenity,
            ) {
                AmenityDetailScreen(
                    osmId = selectedOsmId,
                    onClose = ::deselectAmenity,
                )
            }
            AppInfoModal(
                isOpen = isAppInfoOpen,
                onClose = {
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
    setBounds: (BoundingBox) -> Unit,
    setLocationProblem: (LocationProblem) -> Unit,
    onMarkerClick: (Amenity) -> Unit,
) {
    val context = LocalContext.current
    val isLocationEnabled = context.isLocationEnabled()

    val fineLocationPermission = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    val locationPermissions = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )
    val isMyLocationEnabled = locationPermissions.permissions.any { it.status.isGranted }
    LaunchedEffect(isLocationEnabled, isMyLocationEnabled, locationPermissions) {
        if (!isLocationEnabled) {
            setLocationProblem(LocationProblem.LocationIsOff)
            return@LaunchedEffect
        }
        val fine = isMyLocationEnabled && !locationPermissions.shouldShowRationale
        setLocationProblem(if (fine) LocationProblem.None else LocationProblem.PermissionNeeded)
    }

    val zoomLevel = 15.0
    val cameraState = rememberCameraState(
        firstPosition = CameraPosition(zoom = zoomLevel),
    )
    val styleState = rememberStyleState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(cameraState.isCameraMoving) {
        if (cameraState.isCameraMoving) return@LaunchedEffect
        val projection = cameraState.projection
        if (projection == null) return@LaunchedEffect
        val bounds = projection.queryVisibleBoundingBox()
        setBounds(bounds)
    }

    LaunchedEffect(isMyLocationEnabled) {
        if (isMyLocationEnabled) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnFailureListener { exception ->
                println("fusedLocationClient.lastLocation.addOnFailureListener")
                println(exception)
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                println("fusedLocationClient.lastLocation.addOnSuccessListener")
                if (location == null) {
                    return@addOnSuccessListener
                }
                scope.launch {
                    cameraState.animateTo(
                        finalPosition = CameraPosition(
                            target = Position(
                                latitude = location.latitude,
                                longitude = location.longitude
                            ),
                            zoom = zoomLevel,
                        )
                    )
                }
            }
        } else {
            fineLocationPermission.launchPermissionRequest()
        }
    }

    var clusterFountains by remember { mutableStateOf<List<AmenityClusterItem>>(emptyList()) }
    var clusterRestrooms by remember { mutableStateOf<List<AmenityClusterItem>>(emptyList()) }
    LaunchedEffect(amenities) {
        clusterFountains =
            amenities.fastFilteredMap({ it is Amenity.Fountain }, ::AmenityClusterItem)
        clusterRestrooms =
            amenities.fastFilteredMap({ it is Amenity.Restroom }, ::AmenityClusterItem)
    }

    Box(Modifier.fillMaxSize()) {
        MaplibreMap(
            baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/bright"),
            cameraState = cameraState,
            styleState = styleState,
            options = MapOptions(
                ornamentOptions = OrnamentOptions.AllDisabled,
            )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            DisappearingScaleBar(
                metersPerDp = cameraState.metersPerDpAtTarget,
                zoom = cameraState.position.zoom,
                modifier = Modifier.align(Alignment.TopStart),
            )
            DisappearingCompassButton(
                cameraState = cameraState,
                modifier = Modifier.align(Alignment.TopEnd),
            )
            ExpandingAttributionButton(
                cameraState = cameraState,
                styleState = styleState,
                modifier = Modifier.align(Alignment.BottomEnd),
                contentAlignment = Alignment.BottomEnd,
            )
        }
    }
}

@Composable
private fun MarkerContent(amenity: Amenity) {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = Modifier.padding(top = 2.dp, end = 4.dp),
    ) {
        when (amenity) {
            is Amenity.Fountain -> FountainContent(fountain = amenity)
            is Amenity.Restroom -> RestroomContent(restroom = amenity)
        }
        if (amenity.properties.fee is FeeValue.Yes) {
            Surface(
                Modifier
                    .size(15.dp)
                    .offset(x = 4.dp, y = (-2).dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                border = BorderStroke(1.dp, Color.White),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$",
                        fontSize = 11.sp,
                        lineHeight = 11.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun FountainContent(fountain: Amenity.Fountain) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.marker_fountain),
            contentDescription = fountain.name,
            colorFilter = if (fountain.properties.closed) ColorFilter.tint(Color.Gray) else null,
        )
    }
}

@Composable
private fun RestroomContent(restroom: Amenity.Restroom) {
    Surface(
        Modifier.size(28.dp),
        shape = CircleShape,
        color = if (restroom.properties.closed) Color.Gray else ColorMarkerRestroom,
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
            is Amenity.Fountain -> ColorMarkerFountain
            is Amenity.Restroom -> ColorMarkerRestroom
            null -> Color.Gray
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

private class AmenityClusterItem(val amenity: Amenity) : ClusterItem {
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

private val BoundingBox.center: Location
    get() = Location(
        latitude = (northeast.latitude + southwest.latitude) / 2,
        longitude = (northeast.longitude + southwest.longitude) / 2,
    )

private val BoundingBox.domain: Pair<Location, Location>
    get() = northeast.location to southwest.location

private val Position.location: Location
    get() = Location(latitude = latitude, longitude = longitude)

val Instant.readableDateTime: String
    get() {
        val dateTime = toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        return dateTime.format(formatter)
    }

val Instant.readableDate: String
    get() {
        val dateTime = toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        return dateTime.format(formatter)
    }

fun Context.isLocationEnabled(): Boolean {
    val manager = this.getSystemService(LocationManager::class.java)
    return manager.isLocationEnabled
}
