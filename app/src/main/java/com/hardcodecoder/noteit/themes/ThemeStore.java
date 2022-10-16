package com.hardcodecoder.noteit.themes;


import androidx.annotation.StyleRes;

import com.hardcodecoder.noteit.R;

public class ThemeStore {

    public static final int DARK_GRAY = 0;
    public static final int LIGHT_WHITE = 1;
    public static final int PURE_BLACK = 2;

    @StyleRes
    static int getThemeById(int id) {
        if (id == DARK_GRAY)
            return R.style.ThemeDark;
        else if (id == LIGHT_WHITE)
            return R.style.ThemeLight;
        else return R.style.ThemeBlack;
    }
}
