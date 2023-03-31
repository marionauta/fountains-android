package mn.openlocations.screens.area.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import mn.openlocations.R
import mn.openlocations.domain.models.Area
import mn.openlocations.domain.repositories.AreaRepository
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.RowItem

@Composable
fun AddAreaScreen(navController: NavController) {
    var isLoading by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.areas_add_title)) },
            navigationIcon = {
                IconButton(onClick = navController::navigateUp) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.general_close),
                    )
                }
            },
            actions = {
                AppBarLoader(isLoading = isLoading, modifier = Modifier.padding(end = 16.dp))
            }
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            AddArea(
                navController = navController,
                setIsLoading = { value -> isLoading = value }
            )
        }
    }
}

@Composable
fun AddArea(navController: NavController, setIsLoading: (Boolean) -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    // Areas
    var selectedArea by remember { mutableStateOf<Area?>(null) }
    val (areas, setAreas) = remember { mutableStateOf<List<Area>>(emptyList()) }
    var isSearchingAreas by rememberSaveable { mutableStateOf(false) }
    fun search(query: String) {
        isSearchingAreas = true
        val repository = AreaRepository()
        coroutineScope.launch {
            setAreas(repository.search(query) ?: emptyList())
            isSearchingAreas = false
        }
    }

    LaunchedEffect(isSearchingAreas) {
        setIsLoading(isSearchingAreas)
    }

    // Address Text Field
    var address by rememberSaveable { mutableStateOf("") }

    fun saveSelectedArea(selectedArea: Area?) {
        if (selectedArea == null) {
            return
        }
        val repository = AreaRepository()
        coroutineScope.launch {
            repository.add(selectedArea)
        }
        navController.popBackStack()
    }

    selectedArea?.let {
        Dialog(onDismissRequest = { selectedArea = null }) {
            AreaPreviewModal(area = it) {
                saveSelectedArea(it)
            }
        }
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            TextField(
                value = address,
                onValueChange = { address = it },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(
                    onGo = { search(query = address) }
                ),
                singleLine = true,
                placeholder = {
                    Text(text = stringResource(R.string.areas_add_query_placeholder))
                },
                modifier = Modifier.weight(3f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { search(query = address) },
                enabled = address.isNotBlank() && !isSearchingAreas,
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.areas_add_search_button))
            }
        }

        AreasList(areas = areas) {
            selectedArea = it
        }
    }
}

@Composable
fun AreasList(
    areas: List<Area>,
    checkDiscoveryItem: (Area) -> Unit,
) {
    if (areas.isNotEmpty()) {
        Text(
            text = stringResource(R.string.areas_add_search_results_title),
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
        )
    }
    LazyColumn {
        itemsIndexed(areas, key = { _, item -> item.id }) { index, area ->
            RowItem(
                title = area.name,
                hasTopDivider = index > 0,
                onClick = { checkDiscoveryItem(area) }
            )
        }
    }
}
