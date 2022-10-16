package com.hardcodecoder.noteit.interfaces;

import com.hardcodecoder.noteit.models.NoteModel;

import java.util.List;

public interface OnNotesLoaded {

    void onLoadComplete(List<NoteModel> list);
}
