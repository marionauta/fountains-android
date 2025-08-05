package mn.openlocations.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = ColorPrimary,
    secondary = ColorSecondaryDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
)

private val LightColorPalette = lightColorScheme(
    primary = ColorPrimary,
    secondary = ColorSecondary,
    surface = Color(0xFFefefef),
    onSecondary = Color.White,
)

@Composable
fun FountainsTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
