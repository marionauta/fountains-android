package mn.openlocations.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mn.openlocations.ui.theme.Typography

@Composable
fun EmptyFallback(title: String, description: String, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(all = 16.dp),
    ) {
        Text(
            text = title,
            style = Typography.h5,
        )
        Text(
            text = description,
            style = Typography.body1,
        )
    }
}
