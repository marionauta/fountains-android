package mn.openlocations.screens.amenity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import mn.openlocations.BuildConfig
import mn.openlocations.R
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.FeeValue
import mn.openlocations.domain.models.WheelchairValue
import mn.openlocations.domain.producers.produceMapillaryImageUrl
import mn.openlocations.domain.repositories.FountainRepository
import mn.openlocations.library.parsePropertyValue
import mn.openlocations.networking.KnownUris
import mn.openlocations.screens.map.readableDate
import mn.openlocations.ui.theme.Typography
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.BannerView
import mn.openlocations.ui.views.EmptyFallback

@Composable
fun AmenityDetailScreen(fountainId: String?, onClose: () -> Unit) {
    val (fountain, setFountain) = remember { mutableStateOf<Amenity?>(null) }

    LaunchedEffect(fountainId) {
        if (fountainId == null) {
            return@LaunchedEffect
        }
        val repository = FountainRepository()
        setFountain(repository.get(fountainId = fountainId))
    }

    val uriHandler = LocalUriHandler.current
    fun onFountainProblem() {
        var uri = KnownUris.help("corregir")
        if (fountain != null) {
            uri += "&lat=${fountain.location.latitude}&lng=${fountain.location.longitude}"
        }
        uriHandler.openUri(uri)
    }

    fun onOpenInMaps() {
        var uri = KnownUris.googleMaps
        if (fountain != null) {
            uri += "?api=1&query=${fountain.location.latitude},${fountain.location.longitude}"
        }
        uriHandler.openUri(uri)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                fountain?.name
                    ?.ifBlank {
                        when (fountain) {
                            is Amenity.Fountain -> stringResource(R.string.amenity_detail_fountain_title)
                            is Amenity.Restroom -> stringResource(R.string.amenity_detail_restroom_title)
                        }
                    }
                    ?.let { Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis) }
            },
            navigationIcon = {
                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.general_close),
                    )
                }
            }
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            if (fountain == null) {
                NoFountain()
            } else {
                AmenityDetail(
                    amenity = fountain,
                    onAmenityProblem = ::onFountainProblem,
                    onOpenInMaps = ::onOpenInMaps,
                )
            }
        }
    }
}

@Composable
private fun NoFountain() {
    EmptyFallback(
        title = stringResource(R.string.fountain_detail_not_found_title),
        description = stringResource(R.string.fountain_detail_not_found_description),
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun AmenityDetail(
    amenity: Amenity,
    onAmenityProblem: () -> Unit,
    onOpenInMaps: () -> Unit
) {

    LazyColumn {
        item {
            AmenityImageRow(mapillaryId = amenity.properties.mapillaryId)
        }
        item {
            BannerView(unitId = BuildConfig.ADMOB_DETAIL_AD_UNIT_ID)
        }

        when (amenity) {
            is Amenity.Fountain -> {
                item {
                    PropertyRow(
                        name = stringResource(R.string.fountain_detail_bottle_title),
                        description = stringResource(R.string.fountain_detail_bottle_description),
                        value = amenity.properties.bottle,
                    )
                    Divider()
                }
            }

            is Amenity.Restroom -> {
                item {
                    PropertyRow(
                        name = stringResource(R.string.amenity_detail_handwashing_title),
                        description = stringResource(R.string.amenity_detail_handwashing_description),
                        value = amenity.properties.handwashing,
                    )
                    Divider()
                }
                item {
                    PropertyRow(
                        name = stringResource(R.string.amenity_detail_changing_table_title),
                        description = stringResource(R.string.amenity_detail_changing_table_description),
                        value = amenity.properties.changingTable,
                    )
                    Divider()
                }
            }
        }

        if (amenity.properties.fee != FeeValue.Unknown) {
            item {
                PropertyRow(
                    name = stringResource(R.string.amenity_detail_fee_title),
                    description = stringResource(R.string.amenity_detail_fee_description),
                    value = amenity.properties.fee.title,
                )
                Divider()
            }
        }
        if (amenity.properties.wheelchair != WheelchairValue.UNKNOWN) {
            item {
                PropertyRow(
                    name = stringResource(R.string.fountain_detail_wheelchair_title),
                    description = stringResource(R.string.fountain_detail_wheelchair_description),
                    value = amenity.properties.wheelchair,
                )
                Divider()
            }
        }
        if (amenity.properties.checkDate != null) {
            item {
                PropertyRow(
                    name = stringResource(R.string.fountain_detail_check_date_title),
                    description = stringResource(R.string.fountain_detail_check_date_description),
                    value = amenity.properties.checkDate?.readableDate ?: "",
                )
                Divider()
            }
        }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            ) {
                Button(onClick = onOpenInMaps) {
                    Text(text = stringResource(R.string.fountain_detail_open_maps_button))
                }
            }
        }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            ) {
                Button(onClick = onAmenityProblem) {
                    Text(text = stringResource(R.string.fountain_detail_something_wrong_button))
                }
            }
        }
    }
}

@Composable
private fun AmenityImageRow(mapillaryId: String?) {
    if (mapillaryId == null) return

    val imageUrl by produceMapillaryImageUrl(mapillaryId = mapillaryId)
    var isLoadingImage by rememberSaveable { mutableStateOf(false) }

    Box(contentAlignment = Alignment.Center) {
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.fountain_detail_photo_description),
            onState = { isLoadingImage = it is AsyncImagePainter.State.Loading },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
        )
        AppBarLoader(isLoading = isLoadingImage)
    }
    Divider()
}

@Composable
private fun <T : Enum<T>> PropertyRow(name: String, description: String, value: T) {
    PropertyRow(name = name, description = description, value = value.name)
}

@Composable
private fun PropertyRow(name: String, description: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            Text(text = name)
            Text(text = parsePropertyValue(value = value))
        }
        Text(
            text = description,
            style = Typography.caption
        )
    }
}
