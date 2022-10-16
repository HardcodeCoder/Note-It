package com.hardcodecoder.noteit.models;


import java.io.Serializable;

public class NoteModel implements Serializable {

    private String mFileName;
    private long mCreationTime;
    private long mLastModifiedTime;
    private NoteTextModel mNoteTextModel;
    public static final String SEP = "@#@#END#@#@";

    public NoteModel(String mFileName, long mCreationTime, long mLastModifiedTime, NoteTextModel mNoteTextModel) {
        this.mFileName = mFileName;
        this.mCreationTime = mCreationTime;
        this.mLastModifiedTime = mLastModifiedTime;
        this.mNoteTextModel = mNoteTextModel;
    }

    public String getFileName() { return mFileName; }

    public long getCreationTime() { return mCreationTime; }

    public long getLastModifiedTime() {
        return mLastModifiedTime;
    }

    public void setLastModifiedTime(long mLastModifiedTime) {
        this.mLastModifiedTime = mLastModifiedTime;
    }

    public NoteTextModel getNoteTextModel() {
        return mNoteTextModel;
    }

    public void setNoteTextModel(NoteTextModel mNoteTextModel) {
        this.mNoteTextModel = mNoteTextModel;
    }
}
