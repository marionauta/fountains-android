package mn.fountains.screens.server.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import mn.fountains.R
import mn.fountains.domain.models.Server
import mn.fountains.domain.repositories.ServerRepository
import mn.fountains.ui.theme.Typography

@Composable
fun ServerListScreen() {
    val (servers, setServers) = remember { mutableStateOf(listOf<Server>()) }
    LaunchedEffect(Unit) {
        val repository = ServerRepository()
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
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.servers_list_addButton_description),
                )
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            if (servers.isEmpty()) {
                EmptyServerList()
            } else {
                ServerList(servers)
            }
        }
    }
}

@Composable
private fun ServerList(servers: List<Server>) {
    LazyColumn {
        itemsIndexed(servers, key = { _, item -> item.address }) { index, server ->
            if (index > 0) {
                Divider()
            }
            ServerRow(server)
        }
    }
}

@Composable
private fun ServerRow(server: Server) {
    Column(
        modifier = Modifier
            .clickable {  }
            .padding(all = 16.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = server.name,
            style = Typography.subtitle1,
        )
        Text(
            text = server.address.toString(),
            style = Typography.caption,
        )
    }
}

@Composable
private fun EmptyServerList() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxSize(),
    ) {
        Text(
            stringResource(R.string.servers_list_empty_title),
            style = Typography.h5,
        )
        Text(
            stringResource(R.string.servers_list_empty_description),
            style = Typography.body1
        )
    }
}
