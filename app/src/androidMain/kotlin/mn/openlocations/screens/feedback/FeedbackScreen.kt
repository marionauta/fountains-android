package mn.openlocations.screens.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mn.openlocations.R
import mn.openlocations.domain.models.FeedbackState
import mn.openlocations.domain.repositories.StringStorageRepository
import mn.openlocations.domain.usecases.SendFeedbackUseCase
import mn.openlocations.ui.theme.customColors
import mn.openlocations.ui.views.AppBarLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    amenityId: String,
    state: FeedbackState,
    onClose: () -> Unit,
) {
    val (state, setState) = remember { mutableStateOf(state) }
    val (comment, setComment) = rememberSaveable { mutableStateOf("") }
    var isSending by rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val sendFeedback = SendFeedbackUseCase(StringStorageRepository(LocalContext.current))

    ModalBottomSheet(onDismissRequest = onClose, dragHandle = {}) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.customColors,
                    title = { Text(stringResource(R.string.feedback_screen_title)) },
                    navigationIcon = {
                        IconButton(onClick = onClose) {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = stringResource(R.string.general_close),
                            )
                        }
                    },
                    actions = {
                        if (isSending) {
                            AppBarLoader(isLoading = true, modifier = Modifier.padding(end = 16.dp))
                        } else {
                            TextButton(
                                onClick = {
                                    coroutineScope.launch {
                                        isSending = true
                                        sendFeedback(
                                            osmId = amenityId,
                                            state = state,
                                            comment = comment
                                        )
                                        onClose()
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.feedback_screen_send),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                )
            }
        ) {
            Box(modifier = Modifier.padding(it)) {
                FeedbackView(
                    state = state,
                    setState = setState,
                    comment = comment,
                    setComment = setComment
                )
            }
        }
    }
}

@Composable
private fun FeedbackView(
    state: FeedbackState,
    setState: (FeedbackState) -> Unit,
    comment: String,
    setComment: (String) -> Unit,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            FeedbackButton(
                modifier = Modifier.weight(1f, fill = true),
                variant = FeedbackState.Good,
                selected = state == FeedbackState.Good,
                onClick = setState,
            )
            FeedbackButton(
                modifier = Modifier.weight(1f, fill = true),
                variant = FeedbackState.Bad,
                selected = state == FeedbackState.Bad,
                onClick = setState,
            )
        }

        TextField(
            value = comment,
            onValueChange = setComment,
            textStyle = MaterialTheme.typography.bodySmall,
            placeholder = {
                Text(text = stringResource(R.string.feedback_screen_comment_placeholder))
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
