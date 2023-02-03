package mn.openlocations.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mn.openlocations.ui.theme.ColorPrimary

@Composable
fun Modal(isOpen: Boolean, content: @Composable () -> Unit) {
    if (!isOpen) {
        return
    }
    Box(
        modifier = Modifier
            .background(ColorPrimary.copy(alpha = .5f))
            .fillMaxSize(),
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .padding(32.dp)
                .fillMaxSize(),
            content = content,
        )
    }
}
