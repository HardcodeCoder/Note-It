package com.hardcodecoder.noteit.jobs;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hardcodecoder.noteit.FileStructure;
import com.hardcodecoder.noteit.TaskRunner;
import com.hardcodecoder.noteit.account.UserInfo;
import com.hardcodecoder.noteit.helper.NotesHelper;
import com.hardcodecoder.noteit.models.NoteModel;
import com.hardcodecoder.noteit.storage.StorageHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadJob extends JobService {

    private boolean lastIndex;

    @Override
    public boolean onStartJob(JobParameters params) {
        final String filesDir = getFilesDir().getAbsolutePath();
        boolean jobStarted = false;
        File[] pendingUploadFiles = new File(FileStructure.getPendingSyncedFolderPath(getFilesDir().getAbsolutePath())).listFiles();
        if (null != pendingUploadFiles && pendingUploadFiles.length > 0) {
            jobStarted = true;
            TaskRunner.executeAsync(() -> {
                List<NoteModel> notesList = new ArrayList<>();
                for (File file : pendingUploadFiles) {
                    NoteModel nm = NotesHelper.readNote(file.getAbsolutePath());
                    if (null != nm)
                        notesList.add(nm);
                }
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference;
                String baseRef = UserInfo.getUserStorageDir();
                for (int index = 0; index < notesList.size(); index++) {
                    if (index == notesList.size() - 1)
                        lastIndex = true;
                    NoteModel nm = notesList.get(index);
                    storageReference = storage.getReference(baseRef + nm.getFileName());
                    storageReference.putBytes(nm.toString().getBytes()).addOnSuccessListener(taskSnapshot -> {
                        StorageHelper.moveSyncedNotes(filesDir, nm.getFileName());
                        if (lastIndex) jobFinished(params, false);
                    });
                }
            });
        }
        return jobStarted;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
