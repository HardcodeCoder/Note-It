package com.hardcodecoder.noteit.task;

import android.os.AsyncTask;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.hardcodecoder.noteit.account.UserInfo;

import java.io.File;
import java.util.List;

public class FetchNotesFromCloud extends AsyncTask<Void, Integer, Boolean> {

    private String mFilesDir;
    private boolean mCompleted = false;

    public FetchNotesFromCloud(String mFilesDir) {
        this.mFilesDir = mFilesDir;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference(UserInfo.getUserStorageDir());
        final Task<ListResult> storageReferenceList = reference.listAll();
        storageReferenceList.addOnCompleteListener(task -> {
            if(storageReferenceList.getResult() != null) {
                List<StorageReference> filesReferenceList = storageReferenceList.getResult().getItems();
                int size = filesReferenceList.size();
                for(int i = 0; i < size; i++) {
                    StorageReference filesRef = filesReferenceList.get(i);
                    FileDownloadTask fileDownloadTask = filesRef.getFile(new File(mFilesDir + "/" + filesRef.getName()));
                    if(i == size-1)
                        fileDownloadTask.addOnCompleteListener(task1 -> mCompleted = true);
                }
            }
        });
        return mCompleted;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

    }

}
