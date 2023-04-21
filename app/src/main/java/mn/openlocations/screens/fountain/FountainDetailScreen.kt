package mn.openlocations.screens.fountain

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.datetime.toJavaLocalDate
import mn.openlocations.R
import mn.openlocations.domain.models.BasicValue
import mn.openlocations.domain.models.Fountain
import mn.openlocations.domain.models.FountainNode
import mn.openlocations.domain.models.ParsedOverpassNode
import mn.openlocations.domain.models.RestroomNode
import mn.openlocations.domain.producers.produceMapillaryImageUrl
import mn.openlocations.domain.repositories.FountainRepository
import mn.openlocations.library.parsePropertyValue
import mn.openlocations.networking.KnownUris
import mn.openlocations.ui.theme.Typography
import mn.openlocations.ui.views.EmptyFallback
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun FountainDetailScreen(fountainId: String?, onClose: () -> Unit) {
    val (fountain, setFountain) = remember { mutableStateOf<ParsedOverpassNode?>(null) }

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
            uri += "&lat=${fountain.geopoint.latitude}&lng=${fountain.geopoint.longitude}"
        }
        uriHandler.openUri(uri)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                fountain?.name
                    ?.ifBlank { stringResource(R.string.fountain_detail_fallback_title) }
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
                FountainDetail(
                    node = fountain,
                    onFountainProblem = ::onFountainProblem,
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
private fun FountainDetail(node: ParsedOverpassNode, onFountainProblem: () -> Unit) {
    val imageUrl by produceMapillaryImageUrl(mapillaryId = node.mapillaryId)
    var loadingImage by rememberSaveable { mutableStateOf(false) }

    LazyColumn {
        if (imageUrl != null) {
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = stringResource(R.string.fountain_detail_photo_description),
                        onLoading = { loadingImage = true },
                        onSuccess = { loadingImage = false },
                        onError = { loadingImage = false },
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                    if (loadingImage) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        when (node) {
            is FountainNode -> {
                item {
                    PropertyRow(
                        name = stringResource(R.string.fountain_detail_bottle_title),
                        description = stringResource(R.string.fountain_detail_bottle_description),
                        value = node.properties.bottle,
                    )
                    Divider()
                }
                item {
                    PropertyRow(
                        name = stringResource(R.string.fountain_detail_wheelchair_title),
                        description = stringResource(R.string.fountain_detail_wheelchair_description),
                        value = node.properties.wheelchair,
                    )
                    Divider()
                }
                if (node.properties.checkDate != null) {
                    item {
                        PropertyRow(
                            name = stringResource(R.string.fountain_detail_check_date_title),
                            description = stringResource(R.string.fountain_detail_check_date_description),
                            value = node.properties.checkDate!!.let {
                                val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                                it.toJavaLocalDate().format(formatter)
                            },
                        )
                        Divider()
                    }
                }
            }
            is RestroomNode -> {}
        }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            ) {
                Button(onClick = onFountainProblem) {
                    Text(text = stringResource(R.string.fountain_detail_something_wrong_button))
                }
            }
        }
    }
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

@Preview
@Composable
private fun RowPreview() {
    PropertyRow(
        name = stringResource(R.string.fountain_detail_wheelchair_title),
        description = stringResource(R.string.fountain_detail_wheelchair_description),
        value = BasicValue.UNKNOWN,
    )
}
