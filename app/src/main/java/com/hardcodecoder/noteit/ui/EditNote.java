package com.hardcodecoder.noteit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.widget.Scroller;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.hardcodecoder.noteit.R;
import com.hardcodecoder.noteit.helper.NotesHelper;
import com.hardcodecoder.noteit.models.NoteModel;
import com.hardcodecoder.noteit.models.NoteTextModel;

import java.util.Locale;

public class EditNote extends NoteBaseActivity {

    public static final String NOTE_FILE_NAME = "NoteFileName";
    public static final String POSITION_EDITED = "PositionEdited";
    private TextInputEditText mTitle;
    private TextInputEditText mNote;
    private String mFileName;
    private NoteModel mOldNoteModel;
    private int pos;
    private boolean mDiscarded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_notes);

        mTitle = findViewById(R.id.et_title);
        mNote = findViewById(R.id.et_notes);

        findViewById(R.id.close_btn).setOnClickListener(v -> saveAndDestroyActivity());
        findViewById(R.id.save_btn).setOnClickListener(v -> saveAndDestroyActivity());
        findViewById(R.id.discard_btn).setOnClickListener(v -> discard());

        if (null != getIntent() && null != getIntent().getStringExtra(NOTE_FILE_NAME)) {
            mFileName = getIntent().getStringExtra(NOTE_FILE_NAME);
            pos = getIntent().getIntExtra(POSITION_EDITED, -1);
            loadNote();
        }
    }

    private void discard() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.alert_dialog_title_continue))
                .setMessage(getString(R.string.alert_dialog_delete_note_desc))
                .setNegativeButton(getString(R.string.alert_dialog_negative), (dialog1, which) -> {
                })
                .setPositiveButton(getString(R.string.alert_dialog_positive), (dialog12, which) -> {
                    mDiscarded = true;
                    NotesHelper.deleteNote(this, mOldNoteModel);
                    saveAndDestroyActivity();
                })
                .create();
        dialog.show();
    }

    private void loadNote() {
        final Handler handler = new Handler();
        NotesHelper.readNote(this, mFileName, noteModel -> handler.post(() -> {
            mOldNoteModel = noteModel;
            mNote.setScroller(new Scroller(this));
            mNote.setVerticalScrollBarEnabled(true);
            mNote.setMovementMethod(new ScrollingMovementMethod());
            mNote.setTextIsSelectable(true);
            if (null != mOldNoteModel) {
                mTitle.setText(mOldNoteModel.getNoteTextModel().getTitle());
                mNote.setText(mOldNoteModel.getNoteTextModel().getNotes());
            } else {
                Toast.makeText(this, getString(R.string.toast_note_not_found), Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(EditNote.this, CreateNotes.class), 100);
                finish();
            }
        }));
    }

    @Nullable
    private NoteModel saveEditedNote() {
        if (mDiscarded) return null;
        String title = getNoteTitle();
        String notes = getNote();
        if (!isNoteSame(title, notes)) {
            NoteTextModel ntm = new NoteTextModel(title, notes);
            mOldNoteModel.setNoteTextModel(ntm);
            mOldNoteModel.setLastModifiedTime(SystemClock.elapsedRealtime());
            NotesHelper.saveEditedNote(this, mOldNoteModel);
            Toast.makeText(this, getString(R.string.noted_saved_toast), Toast.LENGTH_SHORT).show();
        }
        return mOldNoteModel;
    }

    private void saveAndDestroyActivity() {
        NoteModel nm = saveEditedNote();
        Intent i = new Intent();
        i.putExtra(MainActivity.NOTE_MODEL, nm);
        i.putExtra(POSITION_EDITED, pos);
        setResult(RESULT_OK, i);
        finish();
    }

    private String getNote() {
        String notes = "";
        if (null != mNote.getText()) notes = mNote.getText().toString();
        notes = notes.trim();
        return notes;
    }

    private String getNoteTitle() {
        String title = String.format(Locale.ENGLISH, "<%s>", getString(R.string.note_def_title));
        if (null != mTitle.getText() && mTitle.getText().length() > 0)
            title = mTitle.getText().toString();
        title = title.trim();
        return title;
    }

    private boolean isNoteSame(String title, String note) {
        String oldTitle = mOldNoteModel.getNoteTextModel().getTitle();
        String oldNote = mOldNoteModel.getNoteTextModel().getNotes();
        return (oldTitle.equals(title) && oldNote.equals(note));
    }

    @Override
    public void onBackPressed() {
        saveAndDestroyActivity();
    }
}
