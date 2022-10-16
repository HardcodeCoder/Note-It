package com.hardcodecoder.noteit.themes;

import android.content.Context;

import androidx.annotation.StyleRes;

import com.hardcodecoder.noteit.database.UserSettings;
import com.hardcodecoder.noteit.utils.DayTimeUtils;

public class ThemeManagerUtils {

    public static boolean initialiseThemeManager(Context context) {
        int oldThemeId = ThemeManager.getThemeId();
        ThemeManager.init(context);
        int newThemeId = ThemeManager.getThemeId();
        return oldThemeId != newThemeId;
    }

    public static boolean isDarkModeCurrentlyEnabled() {
        return ThemeManager.isDarkModeOn();
    }

    public static boolean isAutoThemeCurrentlyEnabled() {
        return ThemeManager.isAutoThemeEnabled();
    }

    public static boolean isDarkModeEnabledByUser(Context context) {
        return UserSettings.isDarkModeEnabled(context);
    }

    public static boolean isBlackThemeEnabledByUser(Context context) {
        return UserSettings.isUseBlackThemeEnabled(context);
    }

    public static boolean toggleDarkTheme(Context context, boolean enabled) {
        UserSettings.enableDarkMode(context, enabled);
        return (!ThemeManager.isDarkModeOn() && enabled) || (ThemeManager.isDarkModeOn() && !enabled);
    }

    public static boolean togglePureBlack(Context context, boolean enabled) {
        UserSettings.enableUseBlackTheme(context, enabled);
        return isDarkModeCurrentlyEnabled();
    }

    public static boolean toggleAutoTheme(Context context, boolean enabled) {
        UserSettings.saveUserAutoThemeSelection(context, enabled);
        // User want auto theme based on time of day
        // If its night/day and current theme is light/dark respectively
        // them restart to apply new theme
        return enabled && needToChangeTheme();

    }

    @StyleRes
    public static int getTheme() {
        int themeId = ThemeManager.getThemeId();
        return ThemeStore.getThemeById(themeId);
    }

    private static boolean needToChangeTheme() {
        return ((isNight() && !ThemeManager.isDarkModeOn()) || (!isNight() && ThemeManager.isDarkModeOn()));
    }

    private static boolean isNight() {
        return (DayTimeUtils.getTimeOfDay() == DayTimeUtils.DayTime.NIGHT);
    }
}
