package mn.openlocations.screens.info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import com.eygraber.uri.Uri
import mn.openlocations.BuildConfig
import mn.openlocations.R
import mn.openlocations.domain.producers.mapMaxDistanceProducer
import mn.openlocations.domain.repositories.PreferencesRepository
import mn.openlocations.networking.KnownUris
import mn.openlocations.ui.theme.customColors
import mn.openlocations.ui.views.Modal
import mn.openlocations.ui.views.RowItem

@Composable
fun AppInfoModal(isOpen: Boolean, onClose: () -> Unit) {
    Modal(isOpen = isOpen, onClose = onClose) {
        AppInfoCoordinator(onClose = onClose)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppInfoCoordinator(onClose: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    var tapped by rememberSaveable { mutableIntStateOf(0) }

    val context = LocalContext.current
    val repository = PreferencesRepository(context)
    val mapMaxDistance by mapMaxDistanceProducer()
    var localMapMaxDistance by rememberSaveable { mutableFloatStateOf(10f) }
    LaunchedEffect(mapMaxDistance) {
        localMapMaxDistance = mapMaxDistance / 1000
    }

    fun toggleSettings() {
        repository.toggleShowAds()
        tapped = 0
    }

    fun setMapMaxDistance(km: Float) {
        repository.setMapMaxDistance(km * 1000)
    }

    val infos = listOf(
        AppInfo(
            title = stringResource(R.string.app_info_max_distance_title, localMapMaxDistance),
            content = stringResource(R.string.app_info_max_distance_content),
            bottom = {
                Slider(
                    value = localMapMaxDistance,
                    valueRange = 4f..40f,
                    onValueChange = { localMapMaxDistance = it },
                    onValueChangeFinished = { setMapMaxDistance(km = localMapMaxDistance) },
                )
            }
        ),
        AppInfo(
            title = stringResource(R.string.app_info_website_title),
            content = Uri.parse(KnownUris.website).host!!,
            onClick = {
                uriHandler.openUri(KnownUris.website)
            },
        ),
        AppInfo(
            title = stringResource(R.string.app_info_developer_title),
            content = stringResource(R.string.app_info_developer_content),
            onClick = {
                uriHandler.openUri(KnownUris.developer)
            },
        ),
        AppInfo(
            title = stringResource(R.string.app_info_app_version_title),
            content = completeVersion(),
            onClick = {
                tapped += 1
            }
        ),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.customColors,
                title = {
                    Text(text = stringResource(R.string.app_info_title))
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.general_close)
                        )
                    }
                },
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            AppInfoScreen(infos = infos)
            if (tapped >= 50) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text(text = stringResource(R.string.app_info_easteregg_title)) },
                    text = { Text(text = stringResource(R.string.app_info_easteregg_content)) },
                    confirmButton = {
                        Button(onClick = { toggleSettings() }) {
                            Text(text = stringResource(R.string.app_info_easteregg_ok))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun AppInfoScreen(infos: List<AppInfo>) {
    LazyColumn {
        itemsIndexed(infos) { index, info ->
            RowItem(
                title = info.title,
                content = info.content,
                contentIsFaded = false,
                trailingContent = info.trailing,
                bottomContent = info.bottom,
                hasTopDivider = index > 0,
                onClick = info.onClick,
            )
        }
    }
}

private data class AppInfo(
    val title: String,
    val content: String,
    val trailing: @Composable () -> Unit = {},
    val bottom: @Composable () -> Unit = {},
    val onClick: () -> Unit = {},
)

private fun completeVersion(): String {
    return "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
}
