package com.hardcodecoder.noteit;

import java.io.File;

public final class FileStructure {

    public static final String NOTE_FILE_NAME_START = "Note_";
    public static final String NOTE_FILE_NAME_END = ".note";
    private static final String FOLDER_NAME_SYNCED = "synced";
    private static final String FOLDER_NAME_PENDING_SYNC = "pending";
    private static final String FOLDER_NAME_PENDING_DELETE = "deletes";

    public static String getSyncedFolderPath(String filesDir) {
        return filesDir + File.separator + FOLDER_NAME_SYNCED;
    }

    public static String getPendingSyncedFolderPath(String filesDir) {
        return filesDir + File.separator + FOLDER_NAME_PENDING_SYNC;
    }

    public static String getDeletesFolderPath(String filesDir) {
        return filesDir + File.separator + FOLDER_NAME_PENDING_DELETE;
    }
}
