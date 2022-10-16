package com.hardcodecoder.noteit.ui;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hardcodecoder.noteit.R;

public class RoundedBottomSheetDialog extends BottomSheetDialog {

    public RoundedBottomSheetDialog(@NonNull Context context) {
        super(context, R.style.BaseBottomSheetDialog);
    }
}
