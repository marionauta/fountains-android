package mn.openlocations.screens.map

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.location.LocationManager
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import mn.openlocations.data.models.OsmId
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.FeeValue
import mn.openlocations.domain.models.Location
import mn.openlocations.ui.views.LocationButton
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.location.LocationPuck
import org.maplibre.compose.location.LocationTrackingEffect
import org.maplibre.compose.location.rememberDefaultLocationProvider
import org.maplibre.compose.location.rememberNullLocationProvider
import org.maplibre.compose.location.rememberUserLocationState
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.material3.DisappearingCompassButton
import org.maplibre.compose.material3.ExpandingAttributionButton
import org.maplibre.compose.material3.LocationPuckDefaults
import org.maplibre.compose.sources.rememberComputedSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.spatialk.geojson.BoundingBox
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Map(
    amenities: List<Amenity>,
    setBounds: (BoundingBox) -> Unit,
    setLocationProblem: (LocationProblem) -> Unit,
    onMarkerClick: (OsmId) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val isLocationEnabled = context.isLocationEnabled()
    val locationPermissions = rememberMultiplePermissionsState(
        listOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    )
    val isLocationPermissionGranted = locationPermissions.permissions.any { it.status.isGranted }
    LaunchedEffect(isLocationEnabled, isLocationPermissionGranted, locationPermissions) {
        if (!isLocationEnabled) {
            setLocationProblem(LocationProblem.LocationIsOff)
            return@LaunchedEffect
        }
        val fine = isLocationPermissionGranted && !locationPermissions.shouldShowRationale
        setLocationProblem(if (fine) LocationProblem.None else LocationProblem.PermissionNeeded)
    }

    val defaultZoomLevel = 15.0
    val cameraState = rememberCameraState()
    val styleState = rememberStyleState()

    LaunchedEffect(cameraState.isCameraMoving) {
        if (cameraState.isCameraMoving) return@LaunchedEffect
        val projection = cameraState.projection
        if (projection == null) return@LaunchedEffect
        val bounds = projection.queryVisibleBoundingBox()
        setBounds(bounds)
    }

    val locationProvider = key(isLocationPermissionGranted) {
        if (isLocationPermissionGranted) {
            rememberDefaultLocationProvider()
        } else {
            rememberNullLocationProvider()
        }
    }
    val userLocation = rememberUserLocationState(locationProvider)

    fun centerOnUserPosition(position: Position) {
        coroutineScope.launch {
            cameraState.animateTo(
                finalPosition = CameraPosition(
                    target = position,
                    zoom = defaultZoomLevel,
                )
            )
        }
    }

    var centeredFirstTime by rememberSaveable { mutableStateOf(false) }
    LocationTrackingEffect(
        locationState = userLocation,
        enabled = isLocationEnabled && isLocationPermissionGranted,
    ) {
        if (centeredFirstTime) return@LocationTrackingEffect
        centeredFirstTime = true
        centerOnUserPosition(currentLocation.position)
    }

    Box(Modifier.fillMaxSize()) {
        MaplibreMap(
            baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/bright"),
            cameraState = cameraState,
            styleState = styleState,
            options = MapOptions(
                ornamentOptions = OrnamentOptions.AllDisabled,
            ),
        ) {
            if (amenities.isNotEmpty()) {
                val amenitiesSource = rememberComputedSource { bbox, _ ->
                    FeatureCollection(
                        features = amenities.map { amenity ->
                            val id = amenity.id.toString()
                            Feature(
                                id = JsonPrimitive(id),
                                geometry = Point(
                                    coordinates = amenity.location.position,
                                ),
                                properties = MarkerProperties(
                                    id = id,
                                    type = when (amenity) {
                                        is Amenity.Fountain -> "fountain"
                                        is Amenity.Restroom -> "restroom"
                                    },
                                    fee = amenity.properties.fee is FeeValue.Yes,
                                    closed = amenity.properties.closed,
                                ),
                            )
                        },
                    )
                }
                MarkerLayer(amenitiesSource, onMarkerClick)
            }
            if (userLocation.location != null) {
                LocationPuck(
                    idPrefix = "mn.openlocations.user",
                    locationState = userLocation,
                    cameraState = cameraState,
                    colors = LocationPuckDefaults.colors(),
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                LocationButton(
                    permissionState = locationPermissions, onClick = {
                        userLocation.location?.let { centerOnUserPosition(it.position) }
                    })
                Spacer(modifier = Modifier.height(8.dp))
                DisappearingCompassButton(
                    cameraState = cameraState,
                )
            }
            ExpandingAttributionButton(
                cameraState = cameraState,
                styleState = styleState,
                modifier = Modifier.align(Alignment.BottomEnd),
                contentAlignment = Alignment.BottomEnd,
            )
        }
    }
}

private val Location.position: Position
    get() = Position(latitude = latitude, longitude = longitude)

private fun Context.isLocationEnabled(): Boolean {
    val manager = this.getSystemService(LocationManager::class.java)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        manager.isLocationEnabled
    } else {
        // TODO: actually check on SDK < 28
        return true
    }
}
