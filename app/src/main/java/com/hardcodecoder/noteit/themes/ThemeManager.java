package com.hardcodecoder.noteit.themes;

import android.content.Context;

import com.hardcodecoder.noteit.database.UserSettings;
import com.hardcodecoder.noteit.utils.DayTimeUtils;

public class ThemeManager {

    private static boolean mDarkModeEnabled;
    private static boolean mAutoThemeEnabled;
    private static int mThemeId;

    public static void init(Context context) {
        mAutoThemeEnabled = UserSettings.getUserAutoThemeSelection(context);

        /*DayTime dayTime = DayTimeUtils.getTimeOfDay();
        if (mUseAutoTheme) {
            mDarkTheme = dayTime == DayTime.NIGHT;
            mThemeId = mDarkTheme ? (mUseBlackTheme ? ThemeStore.PURE_BLACK : ThemeStore.DARK_GRAY) : ThemeStore.LIGHT_WHITE;
        } else {
            mThemeId = UserSettings.getSavedThemeId(context);
            if (mThemeId == ThemeStore.DARK_GRAY && mUseBlackTheme)
                mThemeId = ThemeStore.PURE_BLACK;
            mDarkTheme = mThemeId == ThemeStore.DARK_GRAY || mThemeId == ThemeStore.PURE_BLACK;
        }*/
        if (mAutoThemeEnabled)
            mDarkModeEnabled = DayTimeUtils.getTimeOfDay() == DayTimeUtils.DayTime.NIGHT;
        else mDarkModeEnabled = UserSettings.isDarkModeEnabled(context);

        if (mDarkModeEnabled) {
            if (UserSettings.isUseBlackThemeEnabled(context)) mThemeId = ThemeStore.PURE_BLACK;
            else mThemeId = ThemeStore.DARK_GRAY;
        } else mThemeId = ThemeStore.LIGHT_WHITE;
    }

    static boolean isDarkModeOn() {
        return mDarkModeEnabled;
    }

    static boolean isAutoThemeEnabled() {
        return mAutoThemeEnabled;
    }

    static int getThemeId() {
        return mThemeId;
    }
}
