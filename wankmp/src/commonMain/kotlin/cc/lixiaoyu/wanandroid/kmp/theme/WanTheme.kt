package cc.lixiaoyu.wanandroid.kmp.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class WanColors(
    val pageBackground: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val divider: Color,
    val brand: Color,
    val icon: Color,
)

private val LightWanColors = WanColors(
    pageBackground = Color(0xFFF5F5F5),
    surface = Color.White,
    textPrimary = Color(0xFF212121),
    textSecondary = Color(0xFF757575),
    divider = Color(0xFFEEEEEE),
    brand = Color(0xFF009688),
    icon = Color(0xFF333333),
)

private val DarkWanColors = WanColors(
    pageBackground = Color(0xFF121212),
    surface = Color(0xFF262626),
    textPrimary = Color(0xFFE1E1E1),
    textSecondary = Color(0xFF8D8D8D),
    divider = Color(0xFF343434),
    brand = Color(0xFF009688),
    icon = Color(0xFFE1E1E1),
)

val LocalWanColors = staticCompositionLocalOf { LightWanColors }

val MaterialTheme.wanColors: WanColors
    @Composable
    get() = LocalWanColors.current

@Composable
fun WanTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    val wanColors = if (darkTheme) DarkWanColors else LightWanColors
    CompositionLocalProvider(LocalWanColors provides wanColors) {
        MaterialTheme(
            colors = materialColors(darkTheme, wanColors),
            content = content,
        )
    }
}

private fun materialColors(darkTheme: Boolean, wanColors: WanColors): Colors {
    return if (darkTheme) {
        darkColors(
            primary = wanColors.brand,
            primaryVariant = wanColors.brand,
            secondary = wanColors.brand,
            background = wanColors.pageBackground,
            surface = wanColors.surface,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = wanColors.textPrimary,
            onSurface = wanColors.textPrimary,
        )
    } else {
        lightColors(
            primary = wanColors.brand,
            primaryVariant = wanColors.brand,
            secondary = wanColors.brand,
            background = wanColors.pageBackground,
            surface = wanColors.surface,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = wanColors.textPrimary,
            onSurface = wanColors.textPrimary,
        )
    }
}
