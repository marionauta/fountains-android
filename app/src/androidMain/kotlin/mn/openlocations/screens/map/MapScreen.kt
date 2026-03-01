package mn.openlocations.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import mn.openlocations.ui.theme.customColors
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.BannerView
import mn.openlocations.ui.views.LocationProblemBannerView
import mn.openlocations.ui.views.Modal
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
                    setLocationProblem = { problem -> locationProblem = problem },
                    onMarkerClick = { amenityId -> selectedOsmId = amenityId },
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
