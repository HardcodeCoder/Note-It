package com.hardcodecoder.noteit.storage;

import android.content.Context;
import android.util.Log;

import com.hardcodecoder.noteit.TaskRunner;
import com.hardcodecoder.noteit.TaskRunner.Callback;
import com.hardcodecoder.noteit.models.NoteModel;


public class StorageHelper {

    private static final String TAG = "StorageHelper";
    private static final NotesStorageManager mStorageManager = new NotesStorageManager();

    public static void readNote(Context context, String fileName, Callback<NoteModel> callback) {
        TaskRunner.executeAsync(() -> {
            NoteModel noteModel = mStorageManager.readNote(context.getFilesDir().getAbsolutePath(), fileName);
            callback.onComplete(noteModel);
        });
    }

    public static void saveNewNote(Context context, NoteModel noteModel, Callback<Boolean> callback) {
        TaskRunner.executeAsync(() -> {
            mStorageManager.saveNewNote(context.getFilesDir().getAbsolutePath(), noteModel);
            callback.onComplete(true);
        });
    }

    public static void localDeleteForEditedNote(Context context, String fileName, Callback<Boolean> callback) {
        TaskRunner.executeAsync(() -> {
            boolean res = mStorageManager.deleteNote(context.getFilesDir().getAbsolutePath(), fileName);
            if (res) Log.d(TAG, "Note successfully deleted: " + fileName);
            else Log.e(TAG, "Error deleting notes: " + fileName);
            callback.onComplete(res);
        });
    }

    public static NoteModel readNote(String filePath) {
        return mStorageManager.readNote(filePath);
    }

    public static void moveSyncedNotes(String filesDirPath, String fileName) {
        TaskRunner.executeAsync(() -> {
            boolean res = mStorageManager.moveSyncedNote(filesDirPath, fileName);
            if (res) Log.d(TAG, "Note moved successfully to synced: " + fileName);
            else Log.e(TAG, "Error moving note to synced: " + fileName);
        });
    }

    public static void prepareNoteToDelete(String filesDirPath, String fileName) {
        TaskRunner.executeAsync(() -> {
            boolean res = mStorageManager.moveToDeletes(filesDirPath, fileName);
            if (res) Log.d(TAG, "Note moved successfully to deletes: " + fileName);
            else Log.e(TAG, "Error moving synced note to deletes: " + fileName);
        });
    }

    public static void clearDeletes(String filesDirPath, String fileName) {
        TaskRunner.executeAsync(() -> {
            boolean res = mStorageManager.clearDeletes(filesDirPath, fileName);
            if (res) Log.d(TAG, "Note successfully deleted: " + fileName);
            else Log.e(TAG, "Error deleting notes: " + fileName);
        });
    }
}
