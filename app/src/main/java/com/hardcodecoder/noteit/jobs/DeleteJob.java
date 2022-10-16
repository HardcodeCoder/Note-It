package com.hardcodecoder.noteit.jobs;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hardcodecoder.noteit.FileStructure;
import com.hardcodecoder.noteit.TaskRunner;
import com.hardcodecoder.noteit.account.UserInfo;
import com.hardcodecoder.noteit.storage.StorageHelper;

import java.io.File;

public class DeleteJob extends JobService {


    private boolean lastItem = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        boolean jobStarted = false;
        File[] pendingDeleteFiles = new File(FileStructure.getDeletesFolderPath(getFilesDir().getAbsolutePath())).listFiles();
        if (null != pendingDeleteFiles && pendingDeleteFiles.length > 0) {
            jobStarted = true;
            TaskRunner.executeAsync(() -> {
                final int size = pendingDeleteFiles.length;
                final String filesDir = getFilesDir().getAbsolutePath();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                String baseRef = UserInfo.getUserStorageDir();
                StorageReference storageReference;

                for (int index = 0; index < size; index++) {
                    if (index == size - 1)
                        lastItem = true;

                    String fileName = pendingDeleteFiles[index].getName();
                    storageReference = storage.getReference(baseRef + fileName);
                    storageReference.delete().addOnSuccessListener(aVoid -> {
                        StorageHelper.clearDeletes(filesDir, fileName);
                        if (lastItem) jobFinished(params, false);
                    });
                }
            });
        }
        return jobStarted;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
