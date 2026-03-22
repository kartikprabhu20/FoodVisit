package com.mintanable.foodvisit.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary          = DeepOrange500,
    onPrimary        = White,
    primaryContainer = DeepOrange100,
    onPrimaryContainer = DeepOrange700,
    secondary        = Amber400,
    onSecondary      = Black,
    secondaryContainer = Amber700,
    onSecondaryContainer = White,
    surface          = SurfaceLight,
    onSurface        = Black,
    background       = White,
    onBackground     = Black
)

private val DarkColors = darkColorScheme(
    primary          = DeepOrange100,
    onPrimary        = DeepOrange700,
    primaryContainer = DeepOrange700,
    onPrimaryContainer = DeepOrange100,
    secondary        = Amber400,
    onSecondary      = Black,
    surface          = SurfaceDark,
    onSurface        = OnSurfaceDark,
    background       = Black,
    onBackground     = OnSurfaceDark
)

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun FoodVisitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && darkTheme  -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        darkTheme                  -> DarkColors
        else                       -> LightColors
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = FoodVisitTypography,
        content     = content
    )
}
