package com.hardcodecoder.noteit.loaders;

import com.hardcodecoder.noteit.FileStructure;
import com.hardcodecoder.noteit.helper.NotesHelper;
import com.hardcodecoder.noteit.models.NoteModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class LocalNotesLoader implements Callable<List<NoteModel>> {

    private String path;

    public LocalNotesLoader(String path) {
        this.path = path;
    }

    @Override
    public List<NoteModel> call() {
        List<NoteModel> list = new ArrayList<>();
        File dir = new File(FileStructure.getPendingSyncedFolderPath(path));
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(FileStructure.NOTE_FILE_NAME_END));
        if (null != files && files.length > 0) {
            for (File f : files) {
                NoteModel nm = NotesHelper.readNote(f.getAbsolutePath());
                list.add(nm);
            }
        }

        dir = new File(FileStructure.getSyncedFolderPath(path));
        files = dir.listFiles((dir1, name) -> name.endsWith(FileStructure.NOTE_FILE_NAME_END));
        if (null != files && files.length > 0) {
            for (File f : files) {
                NoteModel nm = NotesHelper.readNote(f.getAbsolutePath());
                list.add(nm);
            }
        }
        Collections.sort(list, (o1, o2) -> {
            long d = o1.getLastModifiedTime() - o2.getLastModifiedTime();
            if (d > 1) return -1;       // to sort in ascending order return +1 here
            else if (d == 0) return 0;
            else return 1;             // to sort in ascending order return -1 here
        });
        return list;
    }
}
