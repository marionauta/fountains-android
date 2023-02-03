package mn.openlocations.screens.fountain

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import mn.openlocations.R
import mn.openlocations.domain.models.BasicValue
import mn.openlocations.domain.models.Fountain
import mn.openlocations.domain.producers.produceMapillaryImageUrl
import mn.openlocations.domain.repositories.FountainRepository
import mn.openlocations.library.parsePropertyValue
import mn.openlocations.ui.theme.Typography
import mn.openlocations.ui.views.EmptyFallback

@Composable
fun FountainDetailScreen(fountainId: String?, onClose: () -> Unit) {
    val (fountain, setFountain) = remember { mutableStateOf<Fountain?>(null) }

    LaunchedEffect(fountainId) {
        if (fountainId == null) {
            return@LaunchedEffect
        }
        val repository = FountainRepository()
        setFountain(repository.get(fountainId = fountainId))
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                fountain?.name
                    ?.ifBlank { stringResource(R.string.fountain_detail_fallback_title) }
                    ?.let { Text(it) }
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
                FountainDetail(fountain = fountain)
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
private fun FountainDetail(fountain: Fountain) {
    val imageUrl by produceMapillaryImageUrl(mapillaryId = fountain.properties.mapillaryId)

    LazyColumn {
        if (imageUrl != null) {
            item {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = stringResource(R.string.fountain_detail_photo_description),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                )
                Divider()
            }
        }
        item {
            PropertyRow(
                name = stringResource(R.string.fountain_detail_bottle_title),
                description = stringResource(R.string.fountain_detail_bottle_description),
                value = fountain.properties.bottle,
            )
            Divider()
        }
        item {
            PropertyRow(
                name = stringResource(R.string.fountain_detail_wheelchair_title),
                description = stringResource(R.string.fountain_detail_wheelchair_description),
                value = fountain.properties.wheelchair,
            )
            Divider()
        }
        if (fountain.properties.checkDate != null) {
            item {
                PropertyRow(
                    name = stringResource(R.string.fountain_detail_check_date_title),
                    description = stringResource(R.string.fountain_detail_check_date_description),
                    value = fountain.properties.checkDate,
                )
                Divider()
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
