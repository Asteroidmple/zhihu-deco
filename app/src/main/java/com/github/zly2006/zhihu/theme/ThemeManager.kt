package com.github.zly2006.zhihu.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.edit
import com.github.zly2006.zhihu.ui.PREFERENCE_NAME

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM,
}

enum class ThemeStyle {
    MIUIX,
    MATERIAL,
}

object ThemeManager {
    private val useDynamicColor = mutableStateOf(true)
    private val customColorInt = mutableIntStateOf(0xFF3482FF.toInt())
    private val backgroundColorLight = mutableIntStateOf(0xFFFFFFFF.toInt())
    private val backgroundColorDark = mutableIntStateOf(0xFF1E1E1E.toInt())
    private val themeMode = mutableStateOf(ThemeMode.SYSTEM)
    private val useMiuix = mutableStateOf(false)
    private val themeStyle = mutableStateOf(ThemeStyle.MATERIAL)

    // MIUIX 特定配置
    private val colorSchemeMode = mutableStateOf(ColorSchemeMode.SYSTEM)
    private val paletteStyle = mutableStateOf(ThemePaletteStyle.TONAL_SPOT)
    private val colorSpec = mutableStateOf(ThemeColorSpec.SPEC_2021)
    private val useMonet = mutableStateOf(false)

    @Composable
    fun getUseDynamicColor(): Boolean = useDynamicColor.value

    fun getUseDynamicColorSync(): Boolean = useDynamicColor.value

    @Composable
    fun getCustomColor(): Color = Color(customColorInt.intValue)

    fun getCustomColorSync(): Color = Color(customColorInt.intValue)

    @Composable
    fun getBackgroundColor(): Color = if (isDarkTheme()) {
        Color(backgroundColorDark.intValue)
    } else {
        Color(backgroundColorLight.intValue)
    }

    @Composable
    fun getThemeMode(): ThemeMode = themeMode.value

    fun getThemeModeSync(): ThemeMode = themeMode.value

    @Composable
    fun getUseMiuix(): Boolean = useMiuix.value

    fun getUseMiuixSync(): Boolean = useMiuix.value

    @Composable
    fun getThemeStyle(): ThemeStyle = themeStyle.value

    fun getThemeStyleSync(): ThemeStyle = themeStyle.value

    // MIUIX 特定配置
    @Composable
    fun getColorSchemeMode(): ColorSchemeMode = colorSchemeMode.value

    fun getColorSchemeModeSync(): ColorSchemeMode = colorSchemeMode.value

    @Composable
    fun getPaletteStyle(): ThemePaletteStyle = paletteStyle.value

    fun getPaletteStyleSync(): ThemePaletteStyle = paletteStyle.value

    @Composable
    fun getColorSpec(): ThemeColorSpec = colorSpec.value

    fun getColorSpecSync(): ThemeColorSpec = colorSpec.value

    @Composable
    fun getUseMonet(): Boolean = useMonet.value

    fun getUseMonetSync(): Boolean = useMonet.value

    //
    var isDarkTheme: Boolean = false

    @Composable
    fun isDarkTheme(): Boolean {
        val isDark = when (themeMode.value) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }
        isDarkTheme = isDark
        return isDark
    }

    fun initialize(context: Context) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        useDynamicColor.value = preferences.getBoolean("useDynamicColor", true)
        customColorInt.intValue = preferences.getInt("customThemeColor", 0xFF3482FF.toInt())
        backgroundColorLight.intValue = preferences.getInt("backgroundColorLight", 0xFFFFFFFF.toInt())
        backgroundColorDark.intValue = preferences.getInt("backgroundColorDark", 0xFF1E1E1E.toInt())
        val themeModeValue = preferences.getString("themeMode", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        themeMode.value = try {
            ThemeMode.valueOf(themeModeValue)
        } catch (_: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
        useMiuix.value = preferences.getBoolean("useMiuix", false)
        themeStyle.value = try {
            ThemeStyle.valueOf(preferences.getString("themeStyle", ThemeStyle.MATERIAL.name) ?: ThemeStyle.MATERIAL.name)
        } catch (_: IllegalArgumentException) {
            ThemeStyle.MATERIAL
        }

        // MIUIX 配置
        val colorSchemeModeValue = preferences.getString("colorSchemeMode", ColorSchemeMode.SYSTEM.name) ?: ColorSchemeMode.SYSTEM.name
        colorSchemeMode.value = try {
            ColorSchemeMode.valueOf(colorSchemeModeValue)
        } catch (_: IllegalArgumentException) {
            ColorSchemeMode.SYSTEM
        }
        val paletteStyleValue = preferences.getString("paletteStyle", ThemePaletteStyle.TONAL_SPOT.name) ?: ThemePaletteStyle.TONAL_SPOT.name
        paletteStyle.value = try {
            ThemePaletteStyle.valueOf(paletteStyleValue)
        } catch (_: IllegalArgumentException) {
            ThemePaletteStyle.TONAL_SPOT
        }
        val colorSpecValue = preferences.getString("colorSpec", ThemeColorSpec.SPEC_2021.name) ?: ThemeColorSpec.SPEC_2021.name
        colorSpec.value = try {
            ThemeColorSpec.valueOf(colorSpecValue)
        } catch (_: IllegalArgumentException) {
            ThemeColorSpec.SPEC_2021
        }
        useMonet.value = preferences.getBoolean("useMonet", false)
    }

    fun setUseDynamicColor(context: Context, useDynamic: Boolean) {
        useDynamicColor.value = useDynamic
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit { putBoolean("useDynamicColor", useDynamic) }
    }

    fun setCustomColor(context: Context, color: Color) {
        customColorInt.intValue = color.toArgb()
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit { putInt("customThemeColor", color.toArgb()) }
    }

    fun setBackgroundColor(context: Context, color: Color, isDark: Boolean) {
        val colorInt = color.toArgb()
        if (isDark) {
            backgroundColorDark.intValue = colorInt
        } else {
            backgroundColorLight.intValue = colorInt
        }
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val key = if (isDark) "backgroundColorDark" else "backgroundColorLight"
        preferences.edit { putInt(key, colorInt) }
    }

    fun setThemeMode(context: Context, mode: ThemeMode) {
        themeMode.value = mode
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit { putString("themeMode", mode.name) }
    }

    fun setUseMiuix(context: Context, use: Boolean) {
        useMiuix.value = use
        themeStyle.value = if (use) ThemeStyle.MIUIX else ThemeStyle.MATERIAL
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit {
            putBoolean("useMiuix", use)
            putString("themeStyle", if (use) ThemeStyle.MIUIX.name else ThemeStyle.MATERIAL.name)
        }
    }

    fun setThemeStyle(context: Context, style: ThemeStyle) {
        themeStyle.value = style
        useMiuix.value = style == ThemeStyle.MIUIX
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit {
            putBoolean("useMiuix", useMiuix.value)
            putString("themeStyle", style.name)
        }
    }

    // MIUIX 特定配置设置
    fun setColorSchemeMode(context: Context, mode: ColorSchemeMode) {
        colorSchemeMode.value = mode
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit { putString("colorSchemeMode", mode.name) }
    }

    fun setPaletteStyle(context: Context, style: ThemePaletteStyle) {
        paletteStyle.value = style
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit { putString("paletteStyle", style.name) }
    }

    fun setColorSpec(context: Context, spec: ThemeColorSpec) {
        colorSpec.value = spec
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit { putString("colorSpec", spec.name) }
    }

    fun setUseMonet(context: Context, use: Boolean) {
        useMonet.value = use
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit { putBoolean("useMonet", use) }
    }

    /**
     * 检查是否支持莫奈取色 (Android 12+)
     */
    fun isMonetSupported(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}
