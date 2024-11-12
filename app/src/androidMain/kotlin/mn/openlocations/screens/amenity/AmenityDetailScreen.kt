package mn.openlocations.screens.amenity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import mn.openlocations.BuildConfig
import mn.openlocations.R
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.BasicValue
import mn.openlocations.domain.models.FeeValue
import mn.openlocations.domain.models.WheelchairValue
import mn.openlocations.domain.producers.produceMapillaryImageUrl
import mn.openlocations.domain.repositories.FountainRepository
import mn.openlocations.networking.KnownUris
import mn.openlocations.screens.map.readableDate
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
    val imageAlpha = 0.6f

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
    ) {
        if (amenity.properties.mapillaryId != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                AmenityImageRow(mapillaryId = amenity.properties.mapillaryId)
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            BannerView(unitId = BuildConfig.ADMOB_DETAIL_AD_UNIT_ID)
        }

        item {
            AmenityPropertyCell(
                title = stringResource(
                    when (amenity.properties.fee) {
                        is FeeValue.No -> R.string.fee_value_no_title
                        is FeeValue.Yes -> R.string.fee_value_yes_title
                        is FeeValue.Unknown -> R.string.fee_value_unknown_title
                        is FeeValue.Donation -> R.string.fee_value_donation_title
                    }
                ),
                subtitle = (amenity.properties.fee as? FeeValue.Yes)?.amount,
                image = {
                    Image(
                        painter = painterResource(R.drawable.property_fee),
                        contentDescription = stringResource(R.string.amenity_detail_fee_description),
                        alpha = imageAlpha,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                        modifier = it,
                    )
                },
                badge = {
                    when (amenity.properties.fee) {
                        is FeeValue.Yes, is FeeValue.Donation -> AmenityPropertyBadge(Variant.Limited)
                        is FeeValue.Unknown -> AmenityPropertyBadge(Variant.Unknown)
                        else -> {}
                    }
                }
            )
        }

        when (amenity) {
            is Amenity.Fountain -> {
                item {
                    AmenityPropertyCell(
                        title = stringResource(R.string.fountain_detail_bottle_title),
                        image = {
                            Image(
                                painter = painterResource(R.drawable.property_bottle),
                                contentDescription = stringResource(R.string.fountain_detail_bottle_description),
                                alpha = imageAlpha,
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                                modifier = it,
                            )
                        },
                        badge = {
                            when (amenity.properties.bottle) {
                                BasicValue.YES -> AmenityPropertyBadge(Variant.Positive)
                                BasicValue.NO -> AmenityPropertyBadge(Variant.Negative)
                                BasicValue.UNKNOWN -> AmenityPropertyBadge(Variant.Unknown)
                            }
                        }
                    )
                }
            }

            is Amenity.Restroom -> {
                item {
                    AmenityPropertyCell(
                        title = stringResource(R.string.amenity_detail_handwashing_title),
                        image = {
                            Image(
                                painter = painterResource(R.drawable.property_handwashing),
                                contentDescription = stringResource(R.string.amenity_detail_handwashing_description),
                                alpha = imageAlpha,
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                                modifier = it,
                            )
                        },
                        badge = {
                            when (amenity.properties.handwashing) {
                                BasicValue.YES -> AmenityPropertyBadge(Variant.Positive)
                                BasicValue.NO -> AmenityPropertyBadge(Variant.Negative)
                                BasicValue.UNKNOWN -> AmenityPropertyBadge(Variant.Unknown)
                            }
                        }
                    )
                }

                item {
                    AmenityPropertyCell(
                        title = stringResource(R.string.amenity_detail_changing_table_title),
                        image = {
                            Image(
                                painter = painterResource(R.drawable.property_changing_table),
                                contentDescription = stringResource(R.string.amenity_detail_changing_table_description),
                                alpha = imageAlpha,
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                                modifier = it,
                            )
                        },
                        badge = {
                            when (amenity.properties.changingTable) {
                                BasicValue.YES -> AmenityPropertyBadge(Variant.Positive)
                                BasicValue.NO -> AmenityPropertyBadge(Variant.Negative)
                                BasicValue.UNKNOWN -> AmenityPropertyBadge(Variant.Unknown)
                            }
                        }
                    )
                }
            }
        }

        item {
            AmenityPropertyCell(
                title = stringResource(R.string.fountain_detail_wheelchair_title),
                image = {
                    Image(
                        painter = painterResource(R.drawable.property_wheelchair),
                        contentDescription = stringResource(R.string.fountain_detail_wheelchair_description),
                        alpha = imageAlpha,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                        modifier = it,
                    )
                },
                badge = {
                    when (amenity.properties.wheelchair) {
                        WheelchairValue.YES -> AmenityPropertyBadge(Variant.Positive)
                        WheelchairValue.LIMITED -> AmenityPropertyBadge(Variant.Limited)
                        WheelchairValue.NO -> AmenityPropertyBadge(Variant.Negative)
                        WheelchairValue.UNKNOWN -> AmenityPropertyBadge(Variant.Unknown)
                    }
                }
            )
        }

        if (amenity.properties.checkDate != null) {
            item {
                AmenityPropertyCell(
                    title = stringResource(R.string.fountain_detail_check_date_title),
                    subtitle = amenity.properties.checkDate?.readableDate,
                    image = {
                        Image(
                            painter = painterResource(R.drawable.property_check_date),
                            contentDescription = stringResource(R.string.fountain_detail_check_date_description),
                            alpha = imageAlpha,
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                            modifier = it,
                        )
                    },
                    badge = {},
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Button(onClick = onOpenInMaps) {
                    Text(text = stringResource(R.string.fountain_detail_open_maps_button))
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(),
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
                .clip(RoundedCornerShape(8.dp))
                .height(250.dp),
        )
        AppBarLoader(isLoading = isLoadingImage)
    }
}
