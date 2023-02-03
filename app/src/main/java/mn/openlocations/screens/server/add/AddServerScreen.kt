package mn.openlocations.screens.server.add

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.launch
import mn.openlocations.R
import mn.openlocations.domain.models.Server
import mn.openlocations.domain.models.ServerDiscoveryItem
import mn.openlocations.domain.models.ServerInfo
import mn.openlocations.domain.producers.discoveredServersProducer
import mn.openlocations.domain.repositories.ServerInfoRepository
import mn.openlocations.domain.repositories.ServerRepository
import mn.openlocations.library.maybeUrl
import mn.openlocations.ui.views.AppBarLoader
import mn.openlocations.ui.views.RowItem

@Composable
fun AddServerScreen(navController: NavController) {
    var isLoading by rememberSaveable { mutableStateOf(false) }

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
            },
            actions = {
                AppBarLoader(isLoading = isLoading, modifier = Modifier.padding(end = 16.dp))
            }
        )
    }) {
        Box(modifier = Modifier.padding(it)) {
            AddServer(
                navController = navController,
                setIsLoading = { value -> isLoading = value }
            )
        }
    }
}

@Composable
fun AddServer(navController: NavController, setIsLoading: (Boolean) -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    // Server discovery
    val discovered by discoveredServersProducer()
    val (discoveredServers, isLoadingDiscovered) = discovered

    // Server Info
    var serverInfo by remember { mutableStateOf<ServerInfo?>(null) }
    var isLoadingServerInfo by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isLoadingDiscovered, isLoadingServerInfo) {
        setIsLoading(isLoadingDiscovered || isLoadingServerInfo)
    }

    // Address Text Field
    var address by rememberSaveable { mutableStateOf("") }
    val addressFocus = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        addressFocus.requestFocus()
    }

    fun checkServerInfo(serverAddress: String) {
        var sanitizedAddress = serverAddress
        if (!sanitizedAddress.startsWith("https://") && !sanitizedAddress.startsWith("http://")) {
            sanitizedAddress = "https://$sanitizedAddress"
        }
        address = (sanitizedAddress)
        val url = maybeUrl(sanitizedAddress) ?: return
        val repository = ServerInfoRepository()
        isLoadingServerInfo = true
        coroutineScope.launch {
            serverInfo = repository.get(baseUrl = url)
            isLoadingServerInfo = false
        }
    }

    val context = LocalContext.current
    fun saveServerInfo(serverInfo: ServerInfo?) {
        if (serverInfo == null) return
        val repository = ServerRepository(context)
        val server = Server(
            address = serverInfo.address,
            name = serverInfo.area.displayName,
            location = serverInfo.area.location,
        )
        coroutineScope.launch {
            repository.add(server)
        }
        navController.popBackStack()
    }

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = address,
            onValueChange = { address = it },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Go,
            ),
            keyboardActions = KeyboardActions(
                onGo = { checkServerInfo(serverAddress = address) }
            ),
            singleLine = true,
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
                onClick = { checkServerInfo(serverAddress = address) },
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
                text = serverInfo!!.area.displayName,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            )
            PreviewMap(serverInfo = serverInfo!!)
        } else {
            DiscoveredServersList(servers = discoveredServers) {
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
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
    )
    LazyColumn {
        itemsIndexed(servers, key = { _, item -> item.address }) { index, server ->
            RowItem(
                title = server.name,
                content = server.address.toString(),
                hasTopDivider = index > 0,
                onClick = { checkDiscoveryItem(server) }
            )
        }
    }
}

@Composable
private fun PreviewMap(serverInfo: ServerInfo) {
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
