package com.hardcodecoder.noteit.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.hardcodecoder.noteit.R;
import com.hardcodecoder.noteit.themes.ThemeManagerUtils;
import com.hardcodecoder.noteit.views.SettingsToggleableItem;

public class SettingsActivity extends NoteBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.settings_back_btn).setOnClickListener(v -> finish());
        initialiseViews();
    }

    private void initialiseViews() {
        SettingsToggleableItem enableDarkModeLayout = findViewById(R.id.settings_enable_dark_mode_layout);
        SettingsToggleableItem useBlackColorLayout = findViewById(R.id.settings_use_black_color_layout);
        SettingsToggleableItem enableAutoModeLayout = findViewById(R.id.settings_enable_auto_theme_layout);

        SwitchMaterial enableDarkThemeSwitch = enableDarkModeLayout.findViewById(R.id.settings_toggleable_item_switch);
        SwitchMaterial useBlackColorSwitch = useBlackColorLayout.findViewById(R.id.settings_toggleable_item_switch);
        SwitchMaterial enableAutoModeSwitch = enableAutoModeLayout.findViewById(R.id.settings_toggleable_item_switch);

        enableDarkThemeSwitch.setChecked(ThemeManagerUtils.isDarkModeEnabledByUser(this));
        useBlackColorSwitch.setChecked(ThemeManagerUtils.isBlackThemeEnabledByUser(this));

        enableAutoModeSwitch.setChecked(ThemeManagerUtils.isAutoThemeCurrentlyEnabled());
        useBlackColorLayout.setEnabled(ThemeManagerUtils.isDarkModeCurrentlyEnabled());
        enableDarkModeLayout.setEnabled(!ThemeManagerUtils.isAutoThemeCurrentlyEnabled());


        enableDarkThemeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (ThemeManagerUtils.toggleDarkTheme(this, isChecked)) {
                restart();
            }
        });

        useBlackColorSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (ThemeManagerUtils.togglePureBlack(this, isChecked)) {
                restart();
            }
        });

        enableAutoModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            enableDarkModeLayout.setEnabled(!isChecked);
            if (ThemeManagerUtils.toggleAutoTheme(this, isChecked)) {
                restart();
            } else {
                // User does not want auto theme based on time of day
                // Revert to theme selected via darkThemeToggle
                if (enableDarkThemeSwitch.isChecked() && !ThemeManagerUtils.isDarkModeCurrentlyEnabled()) {
                    // User previously select dark theme so when auto theme is
                    // disabled apply dark theme if not already applied
                    restart();
                } else if (!enableDarkThemeSwitch.isChecked() && ThemeManagerUtils.isDarkModeCurrentlyEnabled()) {
                    // User previously select light theme so when auto theme is
                    // disabled apply light theme if not already applied
                    restart();
                }
            }
        });

    }

    private void restart() {
        if (ThemeManagerUtils.initialiseThemeManager(this)) {
            recreate();
        }
    }
}
