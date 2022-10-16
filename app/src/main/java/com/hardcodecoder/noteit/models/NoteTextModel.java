package com.hardcodecoder.noteit.models;

import java.io.Serializable;

public class NoteTextModel implements Serializable {

    private String mTitle;
    private String mNotes;

    public NoteTextModel(String mTitle, String mNotes) {
        this.mTitle = mTitle;
        this.mNotes = mNotes;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getNotes() {
        return mNotes;
    }
}
