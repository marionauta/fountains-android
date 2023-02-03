package mn.openlocations.ui.views

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun Modal(isOpen: Boolean, onClose: () -> Unit, content: @Composable () -> Unit) {
    if (!isOpen) {
        return
    }
    Dialog(onDismissRequest = onClose) {
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.padding(vertical = 32.dp),
            content = content,
        )
    }
}
