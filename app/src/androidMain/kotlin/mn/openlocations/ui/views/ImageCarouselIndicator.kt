package mn.openlocations.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ImageCarouselIndicator(
    pagerState: PagerState,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    if (pagerState.pageCount <= 1) return
    Row(
        modifier
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor.copy(alpha = 0.5f))
            .padding(6.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
            Box(
                modifier = Modifier
                    .padding(start = if (iteration == 0) 0.dp else 8.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(8.dp)
            )
        }
    }
}
