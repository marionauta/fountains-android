package mn.openlocations.screens.info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import mn.openlocations.BuildConfig
import mn.openlocations.R
import mn.openlocations.ui.views.RowItem

@Composable
fun AppInfoCoordinator(onClose: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    var tapped by rememberSaveable { mutableStateOf(0) }
    val infos = listOf(
        AppInfo(
            title = stringResource(R.string.app_info_developer_title),
            content = stringResource(R.string.app_info_developer_content),
            onClick = {
                uriHandler.openUri("https://mario.nachbaur.dev")
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
            if (tapped >= 10) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text(text = stringResource(R.string.app_info_easteregg_title)) },
                    text = { Text(text = stringResource(R.string.app_info_easteregg_content)) },
                    confirmButton = {
                        Button(onClick = { tapped = 0 }) {
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
                hasTopDivider = index > 0,
                onClick = info.onClick,
            )
        }
    }
}

private data class AppInfo(
    val title: String,
    val content: String,
    val onClick: () -> Unit = {},
)

private fun completeVersion(): String = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
