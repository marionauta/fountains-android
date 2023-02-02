package mn.openlocations.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import mn.openlocations.BuildConfig
import mn.openlocations.ui.theme.ColorSecondary

@Composable
fun BannerAd(unitId: String = BuildConfig.ADMOB_AD_UNIT_ID) {
    val isShown by remember { mutableStateOf(true) }
    val width = LocalConfiguration.current.screenWidthDp
    val height = if (isShown) 80 else 0

    Column(
        modifier = Modifier
            .height(height.dp)
            .fillMaxWidth()
            .background(ColorSecondary.copy(alpha = 0.2f)),
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize(width, height))
                    adUnitId = unitId
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
