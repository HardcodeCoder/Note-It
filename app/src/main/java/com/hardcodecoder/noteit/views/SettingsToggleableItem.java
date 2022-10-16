package com.hardcodecoder.noteit.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textview.MaterialTextView;
import com.hardcodecoder.noteit.R;

public class SettingsToggleableItem extends FrameLayout {

    private MaterialTextView mTitle;
    private MaterialTextView mText;
    private SwitchMaterial mSwitch;

    public SettingsToggleableItem(@NonNull Context context) {
        super(context);
        initialise(context, null);
    }

    public SettingsToggleableItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialise(context, attrs);
    }

    public SettingsToggleableItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise(context, attrs);
    }

    private void initialise(Context context, @Nullable AttributeSet attributeSet) {
        View layout = View.inflate(context, R.layout.toggleable_item_layout, this);

        mTitle = layout.findViewById(R.id.setting_toggleable_item_title);
        mText = layout.findViewById(R.id.setting_toggleable_item_text);
        mSwitch = layout.findViewById(R.id.settings_toggleable_item_switch);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SettingsToggleableItem);

        mTitle.setText(typedArray.getText(R.styleable.SettingsToggleableItem_settingsToggleableItemTitle));
        mText.setText(typedArray.getText(R.styleable.SettingsToggleableItem_settingsToggleableItemText));


        typedArray.recycle();
    }

    @Override
    public void setEnabled(boolean enabled) {
        mTitle.setEnabled(enabled);
        mText.setEnabled(enabled);
        mSwitch.setEnabled(enabled);
        super.setEnabled(enabled);
    }
}
