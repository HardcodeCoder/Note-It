package com.hardcodecoder.noteit.task;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hardcodecoder.noteit.FileStructure;
import com.hardcodecoder.noteit.account.UserInfo;
import com.hardcodecoder.noteit.models.NoteModel;
import com.hardcodecoder.noteit.storage.StorageHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.Callable;

public class NotesUploadTask implements Callable<Void> {

    private String filesDir;
    private NoteModel noteModel;

    public NotesUploadTask(String filesDir, NoteModel noteModel) {
        this.filesDir = filesDir;
        this.noteModel = noteModel;
    }

    @Override
    public Void call() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference;
        String baseRef = UserInfo.getUserStorageDir();
        storageReference = storage.getReference(baseRef + noteModel.getFileName());
        File f = new File(FileStructure.getPendingSyncedFolderPath(filesDir) + File.separator + noteModel.getFileName());
        try {
            storageReference.putStream(
                    new FileInputStream(f))
                    .addOnSuccessListener(taskSnapshot -> StorageHelper.moveSyncedNotes(filesDir, noteModel.getFileName()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
