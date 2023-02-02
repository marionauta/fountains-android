package mn.openlocations.screens.server.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mn.openlocations.R
import mn.openlocations.domain.models.Server
import mn.openlocations.domain.producers.savedServersProducer
import mn.openlocations.navigation.AppScreen
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.BannerAd
import mn.openlocations.ui.views.EmptyFallback
import mn.openlocations.ui.views.ServerRowItem
import java.net.URLEncoder

@Composable
fun ServerListScreen(navController: NavController) {
    val state by savedServersProducer()
    val (servers, isLoadingServers) = state

    fun onServerClick(server: Server) {
        val encoded = URLEncoder.encode(server.address.toString(), "utf-8")
        navController.navigate(AppScreen.Map.route + "/${encoded}")
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
                    onServerClick = ::onServerClick,
                )
            }
        }
    }
}

@Composable
private fun ServerList(servers: List<Server>, onServerClick: (Server) -> Unit) {
    LazyColumn {
        itemsIndexed(servers, key = { _, item -> item.address }) { index, server ->
            ServerRowItem(
                name = server.name,
                address = server.address.toString(),
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
