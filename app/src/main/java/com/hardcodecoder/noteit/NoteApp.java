package com.hardcodecoder.noteit;

import android.app.Application;
import android.util.Log;

import com.hardcodecoder.noteit.themes.ThemeManager;

import java.io.File;

public class NoteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ThemeManager.init(this);
        TaskRunner.executeAsync(() -> {
            String filesDir = getFilesDir().getAbsolutePath();

            File dir = new File(FileStructure.getSyncedFolderPath(filesDir));
            if (!dir.exists() && !dir.mkdir())
                Log.e("NoteItApp", "cannot create directory in internal storage: " + dir.getAbsolutePath());

            dir = new File(FileStructure.getPendingSyncedFolderPath(filesDir));
            if (!dir.exists() && !dir.mkdir())
                Log.e("NoteItApp", "cannot create directory in internal storage: " + dir.getAbsolutePath());

            dir = new File(FileStructure.getDeletesFolderPath(filesDir));
            if (!dir.exists() && !dir.mkdir())
                Log.e("NoteItApp", "cannot create directory in internal storage: " + dir.getAbsolutePath());
        });
    }

    @Override
    public void onTerminate() {
        Log.e("ApplicationNoteIt", "onTerminate");
        super.onTerminate();
    }
}
