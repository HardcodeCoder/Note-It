package com.hardcodecoder.noteit.utils;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hardcodecoder.noteit.TaskRunner;
import com.hardcodecoder.noteit.jobs.DeleteJob;
import com.hardcodecoder.noteit.jobs.UploadJob;
import com.hardcodecoder.noteit.models.NoteModel;
import com.hardcodecoder.noteit.storage.StorageHelper;
import com.hardcodecoder.noteit.task.NotesDeleteTask;
import com.hardcodecoder.noteit.task.NotesUploadTask;

import java.util.List;


public class NotesSyncUtils {

    private static final int UPLOAD_PENDING_NOTES = 500;
    private static final int DELETE_PENDING_NOTES = 600;

    public static void syncNewNote(Context context, final NoteModel nm) {
        StorageHelper.saveNewNote(context, nm, result -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (null != user) {
                if (NetworkUtil.isConnectedToInternet(context)) {
                    TaskRunner.executeAsync(new NotesUploadTask(context.getFilesDir().getAbsolutePath(), nm));
                } else {
                    scheduleUploadJob(context);
                }
            }
        });
    }

    private static void scheduleUploadJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (null != jobScheduler)
            jobScheduler.schedule(new JobInfo.Builder(
                    UPLOAD_PENDING_NOTES,
                    new ComponentName(context, UploadJob.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build());
    }

    public static void syncDeleteNote(Context context, List<String> fileNamesList) {
        TaskRunner.executeAsync(() -> {
            final String filesDir = context.getFilesDir().getAbsolutePath();
            for (String fileName : fileNamesList) {
                StorageHelper.prepareNoteToDelete(filesDir, fileName);
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (null != user) {
                if (NetworkUtil.isConnectedToInternet(context)) {
                    TaskRunner.executeAsync(new NotesDeleteTask(filesDir, fileNamesList));
                } else {
                    scheduleDeleteJob(context);
                }
            }
        });
    }

    private static void scheduleDeleteJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (null != jobScheduler)
            jobScheduler.schedule(new JobInfo.Builder(
                    DELETE_PENDING_NOTES,
                    new ComponentName(context, DeleteJob.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build());
    }
}
