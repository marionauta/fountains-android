package mn.openlocations.ui.views

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Modal(isOpen: Boolean, onClose: () -> Unit, content: @Composable () -> Unit) {
    if (!isOpen) {
        return
    }
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
    ) {
        Card(
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            backgroundColor = Color.Transparent,
            content = content,
        )
    }
}
