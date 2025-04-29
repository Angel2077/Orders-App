package com.usoftwork.ordersapp.ui.theme

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.usoftwork.ordersapp.R

// Creamos el CompositionLocal para el estado del tema
val LocalDarkTheme = compositionLocalOf { mutableStateOf(false) }

private val DarkColorScheme = darkColorScheme(
    background = DarkGray,
    primary = Salmon,
    secondary = LightGray,
    tertiary = DarkedGray,
    onPrimary = LightGray,
    onSecondary = DarkedGray,
    onTertiary = PureWhite
)

private val LightColorScheme = lightColorScheme(
    background = DarkGray,
    primary = Salmon,
    secondary = LightGray,
    tertiary = DarkedGray,
    onPrimary = LightGray,
    onSecondary = DarkedGray,
    onTertiary = PureWhite)
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */


@Composable
fun OrdersAppTheme(
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkThemeState = remember { mutableStateOf(false) }  // Inicializamos el estado aquí

    val darkTheme = darkThemeState.value

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalDarkTheme provides darkThemeState) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}


@Composable
fun DarkModeButton() {
    val darkThemeState = LocalDarkTheme.current

    IconButton(
        onClick = { darkThemeState.value = !darkThemeState.value }
    ) {
        Image(
            painter = painterResource(
                id = if (darkThemeState.value) R.drawable.fullmoon else R.drawable.fullsun
            ),
            contentDescription = if (darkThemeState.value) "Modo oscuro" else "Modo claro",
            modifier = Modifier.size(36.dp) // Puedes ajustar el tamaño como quieras
        )
    }
}
