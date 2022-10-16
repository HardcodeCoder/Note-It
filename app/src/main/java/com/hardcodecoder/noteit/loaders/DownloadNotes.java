package com.hardcodecoder.noteit.loaders;

import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.hardcodecoder.noteit.FileStructure;
import com.hardcodecoder.noteit.account.UserInfo;

import java.io.File;
import java.util.List;

public class DownloadNotes {

    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static boolean lastItem = false;

    private DownloadNotes() {
    }

    public static void fetchNotes(String filesDir, Callback callback) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference(UserInfo.getUserStorageDir());

        final Task<ListResult> storageReferenceList = reference.listAll();

        storageReferenceList.addOnCompleteListener(task -> {
            if (storageReferenceList.getResult() != null) {
                List<StorageReference> filesReferenceList = storageReferenceList.getResult().getItems();
                int size = filesReferenceList.size();
                if (size > 0) {
                    final String syncedFolderPath = FileStructure.getSyncedFolderPath(filesDir) + File.separator;
                    for (int i = 0; i < size; i++) {
                        if (i == size - 1) lastItem = true;
                        StorageReference filesRef = filesReferenceList.get(i);
                        filesRef
                                .getFile(new File(syncedFolderPath + filesRef.getName()))
                                .addOnCompleteListener(task1 -> {
                                    if (lastItem) handler.post(callback::onComplete);
                                });
                    }
                } else handler.post(callback::onComplete);
            }
        });
    }

    public interface Callback {
        void onComplete();
    }
}
