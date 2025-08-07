package mn.openlocations.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColorScheme(
    primary = ColorPrimary,
    onPrimary = Color.White,
    secondary = ColorSecondary,
    onSecondary = Color.White,
    surface = Color(0xFFefefef),
)

private val DarkColorPalette = darkColorScheme(
    primary = ColorPrimary,
    onPrimary = Color.White,
    secondary = ColorSecondaryDark,
    onSecondary = Color.White,
    surface = Color(0xFF282828),
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
