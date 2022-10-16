package com.hardcodecoder.noteit.task;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hardcodecoder.noteit.account.UserInfo;
import com.hardcodecoder.noteit.storage.StorageHelper;

import java.util.List;
import java.util.concurrent.Callable;

public class NotesDeleteTask implements Callable<Void> {

    private String filesDir;
    private List<String> mFileNameList;

    public NotesDeleteTask(String filesDir, List<String> fileNameList) {
        this.filesDir = filesDir;
        this.mFileNameList = fileNameList;
    }

    @Override
    public Void call() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String baseRef = UserInfo.getUserStorageDir();
        StorageReference storageReference;
        for (String fileName : mFileNameList) {
            storageReference = storage.getReference(baseRef + fileName);
            storageReference.delete().addOnSuccessListener(aVoid -> StorageHelper.clearDeletes(filesDir, fileName));
        }
        return null;
    }
}
