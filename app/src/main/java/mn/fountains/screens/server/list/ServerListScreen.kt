package mn.fountains.screens.server.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import mn.fountains.R
import mn.fountains.domain.models.Server
import mn.fountains.domain.repositories.ServerRepository
import mn.fountains.navigation.AppScreen
import mn.fountains.ui.views.EmptyFallback
import mn.fountains.ui.views.ServerRowItem
import java.net.URLEncoder

@Composable
fun ServerListScreen(navController: NavController) {
    val (servers, setServers) = remember { mutableStateOf(listOf<Server>()) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val repository = ServerRepository(context)
        setServers(repository.all())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.servers_list_title))
                },
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
        Box(modifier = Modifier.padding(it)) {
            if (servers.isEmpty()) {
                EmptyServerList()
            } else {
                ServerList(servers, navController)
            }
        }
    }
}

@Composable
private fun ServerList(servers: List<Server>, navController: NavController) {
    fun onServerClick(server: Server) {
        val encoded = URLEncoder.encode(server.address.toString(), "utf-8")
        navController.navigate(AppScreen.Map.route + "/${encoded}")
    }

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
