package mn.openlocations.screens.amenity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import kotlinx.coroutines.launch
import mn.openlocations.BuildConfig
import mn.openlocations.R
import mn.openlocations.domain.models.AccessValue
import mn.openlocations.domain.models.Amenity
import mn.openlocations.domain.models.BasicValue
import mn.openlocations.domain.models.FeeValue
import mn.openlocations.domain.models.FeedbackComment
import mn.openlocations.domain.models.FeedbackState
import mn.openlocations.domain.models.ImageMetadata
import mn.openlocations.domain.models.ImageSource
import mn.openlocations.domain.models.WheelchairValue
import mn.openlocations.domain.producers.produceImageMetadatas
import mn.openlocations.domain.repositories.AmenityRepository
import mn.openlocations.domain.repositories.StringStorageRepository
import mn.openlocations.domain.usecases.GetFeedbackCommentsUseCase
import mn.openlocations.networking.KnownUris
import mn.openlocations.screens.feedback.FeedbackButton
import mn.openlocations.screens.feedback.FeedbackScreen
import mn.openlocations.screens.map.readableDate
import mn.openlocations.screens.map.readableDateTime
import mn.openlocations.ui.theme.customColors
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.BannerView
import mn.openlocations.ui.views.EmptyFallback
import mn.openlocations.ui.views.ImageCarouselIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmenityDetailScreen(amenityId: String?, onClose: () -> Unit) {
    val (amenity, setAmenity) = remember { mutableStateOf<Amenity?>(null) }
    val (feedback, setFeedback) = remember { mutableStateOf<FeedbackState?>(null) }
    var comments by remember { mutableStateOf<List<FeedbackComment>>(emptyList()) }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    fun loadComments(amenityId: String) {
        val getComments = GetFeedbackCommentsUseCase(StringStorageRepository(context))
        coroutineScope.launch {
            comments = getComments(amenityId)
        }
    }

    LaunchedEffect(amenityId) {
        if (amenityId == null) {
            return@LaunchedEffect
        }
        val repository = AmenityRepository()
        setAmenity(repository.get(amenityId = amenityId))
        loadComments(amenityId)
    }

    fun onAmenityFeedback(state: FeedbackState) {
        setFeedback(state)
    }

    val uriHandler = LocalUriHandler.current

    fun onOpenInMaps() {
        val location = amenity?.location ?: return
        val uri = KnownUris.googleMaps(location)
        uriHandler.openUri(uri.toString())
    }

    fun onOpenFixGuide() {
        val location = amenity?.location ?: return
        var uri = KnownUris.fix(location)
        uriHandler.openUri(uri.toString())
    }

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.customColors,
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
                        color = MaterialTheme.colorScheme.onPrimary,
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
                    comments = comments,
                    onAmenityFeedback = ::onAmenityFeedback,
                    onOpenFixGuide = ::onOpenFixGuide,
                )
            }
            if (feedback != null && amenityId != null) {
                FeedbackScreen(
                    amenityId = amenityId,
                    state = feedback,
                    onClose = {
                        setFeedback(null)
                        loadComments(amenityId)
                    }
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
    comments: List<FeedbackComment>,
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
        if (amenity.properties.imageIds.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                AmenityImageRow(
                    imageIds = amenity.properties.imageIds,
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            BannerView(unitId = BuildConfig.ADMOB_DETAIL_AD_UNIT_ID)
        }

        if (amenity.properties.closed) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.amenity_detail_not_working_notice),
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        }

        // Hide fee if it's free but only for customers
        val hideFee = amenity.properties.let {
            it.fee == FeeValue.No && it.access == AccessValue.Customers
        }
        if (!hideFee) {
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
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
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
        }

        if (amenity.properties.access != AccessValue.Yes) {
            item {
                AmenityPropertyCell(
                    title = stringResource(R.string.amenity_detail_access_title),
                    subtitle = when (amenity.properties.access) {
                        AccessValue.Customers -> stringResource(R.string.access_value_customers_title)
                        AccessValue.Permissive -> stringResource(R.string.access_value_permissive_title)
                        AccessValue.Unknown -> stringResource(R.string.property_value_unknown)
                        else -> null // unreachable
                    },
                    image = {
                        Image(
                            painter = painterResource(R.drawable.property_access),
                            contentDescription = stringResource(R.string.amenity_detail_access_title),
                            alpha = imageAlpha,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            modifier = it,
                        )
                    },
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
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
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
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
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
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
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
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
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
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
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
                    style = MaterialTheme.typography.bodyMedium,
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
            }
        }

        items(comments, key = { it.hashCode() }, span = { GridItemSpan(maxLineSpan) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text(
                    text = when (it.state) {
                        FeedbackState.Good -> "👍"
                        FeedbackState.Bad -> "👎"
                    },
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.size(8.dp))
                Column {
                    Text(
                        text = it.createdAt.readableDateTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = it.comment,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            TextButton(onOpenFixGuide) {
                Text(stringResource(R.string.amenity_detail_how_to_fix_button))
            }
        }
    }
}

@Composable
private fun AmenityImageRow(imageIds: List<Pair<ImageSource, String>>) {
    val images by produceImageMetadatas(imageIds)
    val pagerState = rememberPagerState { images.size }
    if (imageIds.isEmpty()) return

    Box(
        contentAlignment = Alignment.BottomCenter,
    ) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 8.dp,
            modifier = Modifier.clip(RoundedCornerShape(8.dp)),
        ) { index ->
            SingleImage(images[index])
        }
        ImageCarouselIndicator(
            pagerState = pagerState,
            backgroundColor = Color.LightGray,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun SingleImage(image: ImageMetadata) {
    var isLoadingImage by rememberSaveable { mutableStateOf(false) }

    Box(contentAlignment = Alignment.Center) {
        AsyncImage(
            model = image.imageUrl.toString(),
            contentDescription = null,
            onState = { isLoadingImage = it is AsyncImagePainter.State.Loading },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color.LightGray),
        )
        CreatorName(image, Modifier.align(Alignment.BottomEnd))
        AppBarLoader(isLoading = isLoadingImage)
    }
}

@Composable
private fun CreatorName(image: ImageMetadata, modifier: Modifier) {
    var showLicense by rememberSaveable { mutableStateOf(false) }
    val creator = image.creatorUsername ?: return
    if (creator.isBlank()) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.75f))
    ) {
        Text(
            text = creator,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .clickable(enabled = image.licenseName != null) { showLicense = true }
        )
    }

    if (showLicense) {
        AlertDialog(
            onDismissRequest = { showLicense = false },
            text = {
                Text(listOf(image.creatorUsername, image.licenseName).joinToString(", "))
            },
            confirmButton = {
                val uriHandler = LocalUriHandler.current
                image.licenseUrl?.let {
                    Button({
                        showLicense = false
                        uriHandler.openUri(it)
                    }) {
                        Text(stringResource(R.string.amenity_detail_photo_license_learn))
                    }
                }
            },
            dismissButton = {
                Button({ showLicense = false }) {
                    Text(stringResource(R.string.general_close))
                }
            },
        )
    }
}
