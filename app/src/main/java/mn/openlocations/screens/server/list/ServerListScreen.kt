package mn.openlocations.screens.server.list

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
import mn.openlocations.domain.producers.savedServersProducer
import mn.openlocations.domain.repositories.PreferencesRepository
import mn.openlocations.navigation.AppScreen
import mn.openlocations.navigation.replace
import mn.openlocations.screens.info.AppInfoModal
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.BannerAd
import mn.openlocations.ui.views.EmptyFallback
import mn.openlocations.ui.views.RowItem

@Composable
fun ServerListScreen(navController: NavController) {
    val context = LocalContext.current
    val state by savedServersProducer()
    val (servers, isLoadingServers) = state

    var isAppInfoOpen by rememberSaveable { mutableStateOf(false) }

    fun onServerClick(id: String) {
        val repository = PreferencesRepository(context)
        repository.setLastServer(id)
        navController.replace(AppScreen.Map.route + "/${id}")
    }

    LaunchedEffect(Unit) {
        val repository = PreferencesRepository(context)
        val lastServer = repository.getLastServer()
        if (lastServer != null) {
            onServerClick(lastServer)
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
                    navController.navigate(AppScreen.ServerAdd.route)
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
                EmptyServerList()
            } else {
                ServerList(
                    servers = servers,
                    onServerClick = { server -> onServerClick(id = server.id) },
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
private fun ServerList(servers: List<Area>, onServerClick: (Area) -> Unit) {
    LazyColumn {
        itemsIndexed(servers, key = { _, item -> item.id }) { index, server ->
            RowItem(
                title = server.name,
                content = server.osmAreaId.toString(),
                hasTopDivider = index > 0,
                onClick = { onServerClick(server) }
            )
        }
    }
}

@Composable
private fun EmptyServerList() {
    EmptyFallback(
        title = stringResource(R.string.servers_list_empty_title),
        description = stringResource(R.string.servers_list_empty_description),
        modifier = Modifier.fillMaxSize(),
    )
}
