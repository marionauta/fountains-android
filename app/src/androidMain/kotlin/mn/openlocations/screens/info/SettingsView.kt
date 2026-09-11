package mn.openlocations.screens.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import mn.openlocations.R
import mn.openlocations.domain.models.AmenityType
import mn.openlocations.domain.models.FilterSettings
import mn.openlocations.domain.usecases.GetFilterSettingsUseCase
import mn.openlocations.domain.usecases.SaveFilterSettingsUseCase

@Composable
fun SettingsView(
    getSettings: GetFilterSettingsUseCase = GetFilterSettingsUseCase,
    saveSettings: SaveFilterSettingsUseCase = SaveFilterSettingsUseCase,
) {
    val settings by getSettings().collectAsState(null)

    fun onChangeSettings(settings: FilterSettings) {
        saveSettings(settings)
    }

    fun onToggleType(type: AmenityType) {
        val amenities = settings?.amenities ?: return
        val result = if (amenities.contains(type)) {
            amenities.minus(type)
        } else {
            amenities.plus(type)
        }
        onChangeSettings(FilterSettings(amenities = result))
    }

    Column {
        Text(
            text = stringResource(R.string.settings_type_filter_title),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        for (type in AmenityType.entries) {
            val state = settings?.amenities.let {
                when (it) {
                    null -> ToggleableState.Indeterminate
                    else if it.contains(type) -> ToggleableState.On
                    else -> ToggleableState.Off
                }
            }

            fun onClick() {
                onToggleType(type)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .triStateToggleable(state = state, onClick = ::onClick),
            ) {
                TriStateCheckbox(
                    state = state,
                    onClick = ::onClick,
                    enabled = settings != null,
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(type.displayName())
            }
        }
    }
}

@Composable
private fun AmenityType.displayName(): String {
    return when (this) {
        AmenityType.DrinkingFountain -> stringResource(R.string.settings_type_filter_fountain_display)
        AmenityType.Restroom -> stringResource(R.string.settings_type_filter_restroom_display)
    }
}
