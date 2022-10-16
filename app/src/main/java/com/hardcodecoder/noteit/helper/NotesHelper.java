package com.hardcodecoder.noteit.helper;

import android.content.Context;
import android.os.SystemClock;

import com.hardcodecoder.noteit.FileStructure;
import com.hardcodecoder.noteit.TaskRunner;
import com.hardcodecoder.noteit.TaskRunner.Callback;
import com.hardcodecoder.noteit.models.NoteModel;
import com.hardcodecoder.noteit.models.NoteTextModel;
import com.hardcodecoder.noteit.storage.StorageHelper;
import com.hardcodecoder.noteit.utils.NotesSyncUtils;

import java.util.ArrayList;
import java.util.List;


public class NotesHelper {

    public static NoteModel saveNewNote(Context context, NoteTextModel ntm) {
        long time = SystemClock.elapsedRealtime();
        String fileName = generateFileName(time);
        NoteModel nm = new NoteModel(fileName, time, time, ntm);
        NotesSyncUtils.syncNewNote(context, nm);
        return nm;
    }

    public static NoteModel readNote(String filePath) {
        return StorageHelper.readNote(filePath);
    }

    public static void readNote(Context context, String fileName, Callback<NoteModel> callback) {
        StorageHelper.readNote(context, fileName, callback);
    }

    public static void saveEditedNote(Context context, NoteModel nm) {
        StorageHelper.localDeleteForEditedNote(context, nm.getFileName(), result -> {
            if (result) NotesSyncUtils.syncNewNote(context, nm);
        });
    }

    public static void deleteNote(Context context, NoteModel noteModel) {
        List<NoteModel> list = new ArrayList<>();
        list.add(noteModel);
        deleteNotes(context, list);
    }

    public static void deleteNotes(Context context, List<NoteModel> itemsToDelete) {
        TaskRunner.executeAsync(() -> {
            List<String> fileNamesList = new ArrayList<>();
            for (NoteModel nm : itemsToDelete)
                fileNamesList.add(nm.getFileName());
            NotesSyncUtils.syncDeleteNote(context, fileNamesList);
        });
    }

    private static String generateFileName(long time) {
        //Notes_$SystemElapsedTimeInSeconds$.notes
        return FileStructure.NOTE_FILE_NAME_START + time + FileStructure.NOTE_FILE_NAME_END;
    }
}
