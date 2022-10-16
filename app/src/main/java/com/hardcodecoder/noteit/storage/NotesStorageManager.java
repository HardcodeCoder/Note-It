package com.hardcodecoder.noteit.storage;

import androidx.annotation.Nullable;

import com.hardcodecoder.noteit.FileStructure;
import com.hardcodecoder.noteit.models.NoteModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class NotesStorageManager {

    void saveNewNote(String filesDir, NoteModel nm) {
        try {
            String filePath = getAbsoluteFilePath(FileStructure.getPendingSyncedFolderPath(filesDir), nm.getFileName());
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(nm);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    NoteModel readNote(String filesDir, String fileName) {
        File file = new File(getAbsoluteFilePath(FileStructure.getSyncedFolderPath(filesDir), fileName));
        if (!file.exists())
            file = new File(FileStructure.getPendingSyncedFolderPath(filesDir), fileName);
        return readNoteObject(file.getAbsolutePath());
    }

    NoteModel readNote(String filePath) {
        return readNoteObject(filePath);
    }

    boolean moveSyncedNote(String filesDir, String filename) {
        File oldDest = new File(getAbsoluteFilePath(
                FileStructure.getPendingSyncedFolderPath(filesDir),
                filename));
        File newDest = new File(getAbsoluteFilePath(
                FileStructure.getSyncedFolderPath(filesDir),
                filename));
        return oldDest.renameTo(newDest);
    }

    boolean moveToDeletes(String filesDir, String fileName) {
        File oldDest = new File(getAbsoluteFilePath(FileStructure.getSyncedFolderPath(filesDir), fileName));
        File newDest = new File(getAbsoluteFilePath(FileStructure.getDeletesFolderPath(filesDir), fileName));
        if (!oldDest.exists())
            oldDest = new File(getAbsoluteFilePath(FileStructure.getPendingSyncedFolderPath(filesDir), fileName));
        return oldDest.renameTo(newDest);
    }

    boolean clearDeletes(String filesDir, String filename) {
        File file = new File(getAbsoluteFilePath(FileStructure.getDeletesFolderPath(filesDir), filename));
        return file.delete();
    }

    boolean deleteNote(String filesDir, String fileName) {
        File file = new File(getAbsoluteFilePath(FileStructure.getSyncedFolderPath(filesDir), fileName));
        if (!file.exists())
            file = new File(FileStructure.getPendingSyncedFolderPath(filesDir), fileName);
        return file.delete();
    }

    private String getAbsoluteFilePath(String baseDir, String fileName) {
        return baseDir + File.separator + fileName;
    }

    @Nullable
    private NoteModel readNoteObject(String filePath) {
        NoteModel nm = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            nm = (NoteModel) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nm;
    }
}
