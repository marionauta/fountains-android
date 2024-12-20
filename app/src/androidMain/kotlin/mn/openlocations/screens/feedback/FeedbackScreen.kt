package mn.openlocations.screens.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
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
import mn.openlocations.ui.views.Modal

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
    val sendFeedbackUseCase = SendFeedbackUseCase(StringStorageRepository(LocalContext.current))

    Modal(isOpen = true, onClose = onClose) {
        Scaffold(
            topBar = {
                TopAppBar(
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
                        TextButton(
                            enabled = !isSending,
                            onClick = {
                                isSending = true
                                coroutineScope.launch {
                                    sendFeedbackUseCase(
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
                                color = MaterialTheme.colors.onPrimary
                            )
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
            textStyle = MaterialTheme.typography.body1,
            placeholder = {
                Text(text = stringResource(R.string.feedback_screen_comment_placeholder))
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
