package mn.openlocations.screens.area.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mn.openlocations.R
import mn.openlocations.domain.models.Area
import mn.openlocations.domain.producers.storedAreasProducer
import mn.openlocations.domain.repositories.PreferencesRepository
import mn.openlocations.navigation.AppScreen
import mn.openlocations.navigation.replace
import mn.openlocations.screens.info.AppInfoModal
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.BannerAd
import mn.openlocations.ui.views.EmptyFallback
import mn.openlocations.ui.views.RowItem

@Composable
fun AreaListScreen(navController: NavController) {
    val context = LocalContext.current
    val state by storedAreasProducer()
    val (servers, isLoadingServers) = state

    var isAppInfoOpen by rememberSaveable { mutableStateOf(false) }

    fun onAreaClick(id: String) {
        val repository = PreferencesRepository(context)
        repository.setLastAreaId(id)
        navController.replace(AppScreen.Map.route + "/${id}")
    }

    LaunchedEffect(Unit) {
        val repository = PreferencesRepository(context)
        val lastAreaId = repository.getLastAreaId()
        if (lastAreaId != null) {
            onAreaClick(lastAreaId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.servers_list_title))
                },
                actions = {
                    AppBarLoader(
                        isLoading = isLoadingServers,
                        modifier = Modifier.padding(end = 16.dp),
                    )
                    IconButton(onClick = { isAppInfoOpen = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = stringResource(R.string.app_info_menu_item),
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AppScreen.AreaAdd.route)
                }
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.servers_list_addButton_description),
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            BannerAd()
            if (!isLoadingServers && servers.isEmpty()) {
                EmptyAreaList()
            } else {
                AreaList(
                    servers = servers,
                    onServerClick = { server -> onAreaClick(id = server.id) },
                )
            }
        }
        AppInfoModal(
            isOpen = isAppInfoOpen,
            onClose = { isAppInfoOpen = false }
        )
    }
}

@Composable
private fun AreaList(servers: List<Area>, onServerClick: (Area) -> Unit) {
    LazyColumn {
        itemsIndexed(servers, key = { _, item -> item.id }) { index, server ->
            RowItem(
                title = server.trimmedDisplayName,
                hasTopDivider = index > 0,
                onClick = { onServerClick(server) }
            )
        }
    }
}

@Composable
private fun EmptyAreaList() {
    EmptyFallback(
        title = stringResource(R.string.servers_list_empty_title),
        description = stringResource(R.string.servers_list_empty_description),
        modifier = Modifier.fillMaxSize(),
    )
}
