package mn.fountains.screens.server.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mn.fountains.R
import mn.fountains.data.datasources.ServerInfoDataSource
import mn.fountains.data.models.ServerInfoDto
import mn.fountains.domain.models.Server
import mn.fountains.domain.models.ServerDiscoveryItem
import mn.fountains.domain.models.intoDomain
import mn.fountains.domain.repositories.DiscoveryRepository
import mn.fountains.domain.repositories.ServerRepository
import java.net.URL

@Composable
fun AddServerScreen(navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.servers_add_title)) },
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
            AddServer(navController = navController)
        }
    }
}

@Composable
fun AddServer(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    val (discovered, setDiscovered) = remember { mutableStateOf<List<ServerDiscoveryItem>>(emptyList()) }
    val (address, setAddress) = remember { mutableStateOf("") }
    val addressFocus = remember { FocusRequester() }
    val (serverInfo, setServerInfo) = remember { mutableStateOf<ServerInfoDto?>(null) }

    LaunchedEffect(Unit) {
        addressFocus.requestFocus()
    }

    LaunchedEffect(Unit) {
        val repository = DiscoveryRepository()
        setDiscovered(repository.all())
    }

    fun checkServerInfo(address: String) {
        val url = URL(address)
        val dataSource = ServerInfoDataSource()
        coroutineScope.launch {
            val info = dataSource.get(url)
            println(info)
            setServerInfo(info)
        }
    }

    val context = LocalContext.current
    fun saveServerInfo(serverInfo: ServerInfoDto?) {
        if (serverInfo == null) return
        val repository = ServerRepository(context)
        val server = Server(
            name = serverInfo.area.displayName,
            address = URL(address),
            location = serverInfo.area.location.intoDomain(),
        )
        runBlocking {
            repository.add(server)
        }
        navController.popBackStack()
    }

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = address,
            onValueChange = setAddress,
            modifier = Modifier
                .focusRequester(addressFocus)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = stringResource(R.string.servers_add_address_placeholder))
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = { checkServerInfo(address = address) },
                enabled = address.isNotBlank(),
            ) {
                Text(stringResource(R.string.servers_add_checkButton))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { saveServerInfo(serverInfo) },
                enabled = serverInfo != null,
            ) {
                Text(stringResource(R.string.servers_add_addButton))
            }
        }

        if (serverInfo != null) {
            Text(
                text = serverInfo.area.displayName,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(16.dp),
            )

            PreviewMap(serverInfo = serverInfo)

        } else if (discovered.isNotEmpty()) {
            DiscoveredServersList(servers = discovered) {
                setAddress(it.address.toString())
                checkServerInfo(it.address.toString())
            }
        }
    }
}

@Composable
fun DiscoveredServersList(
    servers: List<ServerDiscoveryItem>,
    checkDiscoveryItem: (ServerDiscoveryItem) -> Unit,
) {
    Text(
        text = stringResource(R.string.servers_add_known_servers),
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
        style = MaterialTheme.typography.h5,
    )
    LazyColumn {
        itemsIndexed(servers, key = { _, item -> item.address }) { index, server ->
            if (index > 0) {
                Divider()
            }
            ServerRow(server = server, onClick = checkDiscoveryItem)
        }
    }
}

@Composable
private fun ServerRow(server: ServerDiscoveryItem, onClick: (ServerDiscoveryItem) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick(server) }
            .padding(all = 16.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = server.name,
            style = MaterialTheme.typography.subtitle1,
        )
        Text(
            text = server.address.toString(),
            style = MaterialTheme.typography.caption,
        )
    }
}

@Composable
private fun PreviewMap(serverInfo: ServerInfoDto) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = CameraPositionState(
            position = CameraPosition.fromLatLngZoom(
                serverInfo.area.location.let { LatLng(it.latitude, it.longitude) },
                13f
            )
        ),
        uiSettings = MapUiSettings(
            compassEnabled = false,
            myLocationButtonEnabled = false,
            mapToolbarEnabled = false,
            rotationGesturesEnabled = false,
            scrollGesturesEnabled = false,
            scrollGesturesEnabledDuringRotateOrZoom = false,
            tiltGesturesEnabled = false,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = false,
            indoorLevelPickerEnabled = false,
        )
    )
}
