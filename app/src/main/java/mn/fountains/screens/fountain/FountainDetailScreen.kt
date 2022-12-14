package mn.fountains.screens.fountain

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mn.fountains.R
import mn.fountains.domain.models.BasicValue
import mn.fountains.domain.models.Fountain
import mn.fountains.domain.repositories.FountainRepository
import mn.fountains.ui.theme.Typography
import mn.fountains.ui.views.EmptyFallback

@Composable
fun FountainDetailScreen(fountainId: String, navController: NavController) {
    val (fountain, setFountain) = remember { mutableStateOf<Fountain?>(null) }

    LaunchedEffect(fountainId) {
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
                IconButton(onClick = navController::navigateUp) {
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
    LazyColumn {
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
    }
}

@Composable
private fun <T : Enum<T>> PropertyRow(name: String, description: String, value: T) {
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
            Text(text = value.name)
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
