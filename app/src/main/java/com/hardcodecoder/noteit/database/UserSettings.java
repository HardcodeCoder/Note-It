package com.hardcodecoder.noteit.database;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSettings {

    private static final String APP_THEME = "AppTheme";
    private static final String ENABLE_DARK_MODE = "EnableDarkMode";
    private static final String ID_AUTO_THEME = "AutoTheme";
    private static final String ID_BLACK_THEME = "UseBlackTheme";

    public static void enableDarkMode(Context context, boolean enabled) {
        SharedPreferences sharedPref = context.getSharedPreferences(APP_THEME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ENABLE_DARK_MODE, enabled);
        editor.apply();
    }

    public static boolean isDarkModeEnabled(Context context) {
        return context.getSharedPreferences(APP_THEME, Context.MODE_PRIVATE).getBoolean(ENABLE_DARK_MODE, true);
    }

    public static void enableUseBlackTheme(Context context, boolean use) {
        SharedPreferences sharedPref = context.getSharedPreferences(APP_THEME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ID_BLACK_THEME, use);
        editor.apply();
    }

    public static boolean isUseBlackThemeEnabled(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(APP_THEME, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(ID_BLACK_THEME, false);
    }

    public static void saveUserAutoThemeSelection(Context context, boolean enabled) {
        SharedPreferences sharedPref = context.getSharedPreferences(APP_THEME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ID_AUTO_THEME, enabled);
        editor.apply();
    }

    public static boolean getUserAutoThemeSelection(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(APP_THEME, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(ID_AUTO_THEME, false);
    }
}
