package com.hardcodecoder.noteit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.hardcodecoder.noteit.R;
import com.hardcodecoder.noteit.helper.NotesHelper;
import com.hardcodecoder.noteit.models.NoteModel;
import com.hardcodecoder.noteit.models.NoteTextModel;


public class CreateNotes extends NoteBaseActivity {

    private EditText mTitle;
    private EditText mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        mTitle = findViewById(R.id.et_title);
        mNote = findViewById(R.id.et_notes);

        findViewById(R.id.close_btn).setOnClickListener(v -> saveAndDestroyActivity());
        findViewById(R.id.save_btn).setOnClickListener(v -> saveAndDestroyActivity());
        findViewById(R.id.discard_btn).setOnClickListener(v -> discard());
    }

    private void saveAndDestroyActivity() {
        saveResult();
        finish();
    }

    private void discard() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.alert_dialog_title_continue))
                .setMessage(getString(R.string.alert_dialog_delete_note_desc))
                .setNegativeButton(getString(R.string.alert_dialog_negative), (dialog1, which) -> {
                })
                .setPositiveButton(getString(R.string.alert_dialog_positive), (dialog12, which) -> {
                    mTitle.getText().clear();
                    mNote.getText().clear();
                })
                .create();
        dialog.show();
    }

    private void saveResult() {
        NoteModel nm = saveNote();
        Intent i = new Intent();
        i.putExtra(MainActivity.NOTE_MODEL, nm);
        setResult(RESULT_OK, i);
    }

    @Nullable
    private NoteModel saveNote() {
        String notes = getNote();
        if (notes.length() > 0) {
            NoteModel nm;
            NoteTextModel ntm = new NoteTextModel(getNoteTitle(), notes);
            nm = NotesHelper.saveNewNote(this, ntm);
            Toast.makeText(this, getString(R.string.noted_saved_toast), Toast.LENGTH_SHORT).show();
            return nm;
        } else {
            Toast.makeText(this, getString(R.string.noted_discarded_toast), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private String getNote() {
        String notes = "";
        if (null != mNote.getText()) notes = mNote.getText().toString();
        notes = notes.trim();
        return notes;
    }

    private String getNoteTitle() {
        String title = "<No title>";
        if (null != mTitle.getText() && mTitle.getText().length() > 0)
            title = mTitle.getText().toString();
        title = title.trim();
        return title;
    }

    @Override
    public void onBackPressed() {
        saveAndDestroyActivity();
    }
}
