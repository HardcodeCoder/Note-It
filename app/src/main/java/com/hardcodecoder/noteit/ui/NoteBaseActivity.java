package com.hardcodecoder.noteit.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hardcodecoder.noteit.themes.ThemeManagerUtils;

public abstract class NoteBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(ThemeManagerUtils.getTheme());
        super.onCreate(savedInstanceState);
    }
}
