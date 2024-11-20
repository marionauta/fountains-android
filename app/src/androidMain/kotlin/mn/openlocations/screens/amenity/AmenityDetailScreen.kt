package mn.openlocations.screens.amenity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import mn.openlocations.domain.models.AccessValue
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.BasicValue
import mn.openlocations.domain.models.FeeValue
import mn.openlocations.domain.models.FeedbackState
import mn.openlocations.domain.models.WheelchairValue
import mn.openlocations.domain.producers.produceMapillaryImageUrl
import mn.openlocations.domain.repositories.AmenityRepository
import mn.openlocations.networking.KnownUris
import mn.openlocations.screens.feedback.FeedbackButton
import mn.openlocations.screens.feedback.FeedbackScreen
import mn.openlocations.screens.map.readableDate
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.BannerView
import mn.openlocations.ui.views.EmptyFallback

@Composable
fun AmenityDetailScreen(amenityId: String?, onClose: () -> Unit) {
    val (amenity, setAmenity) = remember { mutableStateOf<Amenity?>(null) }
    val (feedback, setFeedback) = remember { mutableStateOf<FeedbackState?>(null) }

    LaunchedEffect(amenityId) {
        if (amenityId == null) {
            return@LaunchedEffect
        }
        val repository = AmenityRepository()
        setAmenity(repository.get(amenityId = amenityId))
    }

    fun onAmenityFeedback(state: FeedbackState) {
        setFeedback(state)
    }

    val uriHandler = LocalUriHandler.current

    fun onOpenInMaps() {
        var uri = KnownUris.googleMaps
        amenity?.let {
            uri += "?api=1&query=${it.location.latitude},${it.location.longitude}"
        }
        uriHandler.openUri(uri)
    }

    fun onOpenFixGuide() {
        var uri = KnownUris.help("fix")
        amenity?.let {
            uri += "&lat=${it.location.latitude}&lng=${it.location.longitude}"
        }
        uriHandler.openUri(uri)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                amenity?.name
                    ?.ifBlank {
                        when (amenity) {
                            is Amenity.Fountain -> stringResource(R.string.amenity_detail_fountain_title)
                            is Amenity.Restroom -> stringResource(R.string.amenity_detail_restroom_title)
                        }
                    }
                    ?.let { Text(it, maxLines = 2, overflow = TextOverflow.Ellipsis) }
            },
            navigationIcon = {
                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.general_close),
                    )
                }
            },
            actions = {
                TextButton(onClick = ::onOpenInMaps) {
                    Text(
                        text = stringResource(R.string.fountain_detail_open_maps_button),
                        color = MaterialTheme.colors.onPrimary,
                    )
                }
            }
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            if (amenity == null) {
                NoFountain()
            } else {
                AmenityDetail(
                    amenity = amenity,
                    onAmenityFeedback = ::onAmenityFeedback,
                    onOpenFixGuide = ::onOpenFixGuide,
                )
            }
            if (feedback != null && amenityId != null) {
                FeedbackScreen(
                    amenityId = amenityId,
                    state = feedback,
                    onClose = { setFeedback(null) }
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
    onAmenityFeedback: (state: FeedbackState) -> Unit,
    onOpenFixGuide: () -> Unit,
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
                AmenityImageRow(
                    amenity = amenity,
                    mapillaryId = amenity.properties.mapillaryId,
                )
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

        if (amenity.properties.access != AccessValue.Yes) {
            item {
                AmenityPropertyCell(
                    title = stringResource(R.string.amenity_detail_access_title),
                    subtitle = stringResource(
                        when (amenity.properties.access) {
                            AccessValue.Customers -> R.string.access_value_customers_title
                            AccessValue.Permissive -> R.string.access_value_permissive_title
                            AccessValue.Unknown -> R.string.property_value_unknown
                            else -> 0 // unreachable
                        }
                    ),
                    image = {},
                    badge = {
                        AmenityPropertyBadge(
                            when (amenity.properties.access) {
                                AccessValue.No, AccessValue.Private -> Variant.Negative
                                AccessValue.Permissive, AccessValue.Customers -> Variant.Limited
                                AccessValue.Yes -> Variant.Positive
                                AccessValue.Unknown -> Variant.Unknown
                            }
                        )
                    }
                )
            }
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
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text(
                    text = stringResource(R.string.amenity_detail_feedback_title),
                    style = MaterialTheme.typography.subtitle2,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                ) {
                    FeedbackButton(
                        modifier = Modifier.weight(1f, fill = true),
                        variant = FeedbackState.Good,
                        selected = false,
                        onClick = onAmenityFeedback,
                    )
                    FeedbackButton(
                        modifier = Modifier.weight(1f, fill = true),
                        variant = FeedbackState.Bad,
                        selected = false,
                        onClick = onAmenityFeedback,
                    )
                }

                TextButton(onOpenFixGuide) {
                    Text(stringResource(R.string.amenity_detail_how_to_fix_button))
                }
            }
        }
    }
}

@Composable
private fun AmenityImageRow(amenity: Amenity, mapillaryId: String?) {
    if (mapillaryId == null) return

    val imageUrl by produceMapillaryImageUrl(mapillaryId = mapillaryId)
    var isLoadingImage by rememberSaveable { mutableStateOf(false) }

    Box(contentAlignment = Alignment.Center) {
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(
                when (amenity) {
                    is Amenity.Fountain -> R.string.amenity_detail_fountain_photo_description
                    is Amenity.Restroom -> R.string.amenity_detail_restroom_photo_description
                }
            ),
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
