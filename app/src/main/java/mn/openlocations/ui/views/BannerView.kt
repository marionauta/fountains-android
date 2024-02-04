package mn.openlocations.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import mn.openlocations.domain.producers.showAdsProducer

private enum class BannerViewStage {
    Loading, Loaded, Error
}

private class BannerViewAdListener(private val onStageChanged: (BannerViewStage) -> Unit) :
    AdListener() {
    override fun onAdFailedToLoad(error: LoadAdError) {
        super.onAdFailedToLoad(error)
        onStageChanged(BannerViewStage.Error)
    }

    override fun onAdLoaded() {
        super.onAdLoaded()
        onStageChanged(BannerViewStage.Loaded)
    }
}

@Composable
fun BannerView(unitId: String) {
    var stage by remember { mutableStateOf(BannerViewStage.Loading) }
    val showAds by showAdsProducer()
    val width = LocalConfiguration.current.screenWidthDp
    val height = 80

    val listener = BannerViewAdListener { stage = it }

    if (stage == BannerViewStage.Error || !showAds) {
        return
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(height.dp)
            .fillMaxWidth()
    ) {
        AppBarLoader(isLoading = stage == BannerViewStage.Loading)
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize(width, height))
                    adUnitId = unitId
                    adListener = listener
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
