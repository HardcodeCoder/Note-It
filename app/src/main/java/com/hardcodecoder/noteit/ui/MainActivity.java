package com.hardcodecoder.noteit.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hardcodecoder.noteit.R;
import com.hardcodecoder.noteit.TaskRunner;
import com.hardcodecoder.noteit.adapters.NotesAdapter;
import com.hardcodecoder.noteit.helper.NotesHelper;
import com.hardcodecoder.noteit.helper.SimpleTouchHelper;
import com.hardcodecoder.noteit.interfaces.ItemClickListener;
import com.hardcodecoder.noteit.loaders.DownloadNotes;
import com.hardcodecoder.noteit.loaders.LocalNotesLoader;
import com.hardcodecoder.noteit.models.NoteModel;
import com.hardcodecoder.noteit.themes.ThemeManagerUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends NoteBaseActivity implements ItemClickListener {

    public static final String NOTE_MODEL = "MoteModel";
    public static final short REQUEST_CODE_CREATE_NOTE = 100;
    public static final short REQUEST_CODE_EDIT_NOTE = 101;
    private static final int GRID_COUNT_PORTRAIT = 2;
    private static final int GRID_COUNT_LANDSCAPE = 3;
    private String mFilesDir;
    private ProgressBar mProgressBar;
    private FloatingActionButton fab;
    private List<NoteModel> mNotesList;
    private NotesAdapter mAdapter;
    private SimpleTouchHelper touchHelper;
    private StaggeredGridLayoutManager mLayoutManager;
    private int orientation;
    private int gridCount;
    private int selectedItems = 0;
    @StyleRes
    private int mTheme;
    private boolean isSelectionModeActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTheme = ThemeManagerUtils.getTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        mFilesDir = getFilesDir().getAbsolutePath();
        mProgressBar = findViewById(R.id.progress_bar);
        orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) gridCount = GRID_COUNT_LANDSCAPE;
        else gridCount = GRID_COUNT_PORTRAIT;
        if (null != getIntent() && getIntent().getBooleanExtra(SignInChooserActivity.NEW_SIGN_IN, false))
            fetchNotesFromCloud();
        else
            loadNotes();
        initializeUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void fetchNotesFromCloud() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (null != user) {
            mProgressBar.setVisibility(View.VISIBLE);
            TaskRunner.executeAsync(() -> DownloadNotes.fetchNotes(mFilesDir, () -> {
                mProgressBar.setVisibility(View.GONE);
                loadNotes();
            }));
        } else Toast.makeText(this, "Sign in first to sync notes", Toast.LENGTH_LONG).show();
    }

    private void initializeUi() {
        BottomAppBar bar = findViewById(R.id.bottom_bar);
        bar.setPopupTheme(R.style.BottomMenuTheme);
        setSupportActionBar(bar);

        bar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.user:
                    startActivity(new Intent(MainActivity.this, UserAccountActivity.class));
                    break;
                case R.id.refresh:
                    fetchNotesFromCloud();
                    break;
                case R.id.settings:
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    break;
                /*case R.id.test:
                    startActivity(new Intent(this, TestActivity.class));
                    break;*/
            }
            return true;
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            if (isSelectionModeActive) delete();
            else
                startActivityForResult(new Intent(this, CreateNotes.class), REQUEST_CODE_CREATE_NOTE);
        });
    }

    private void loadNotes() {
        TaskRunner.executeAsync(new LocalNotesLoader(mFilesDir), list -> new Handler().postDelayed(() -> {
            mNotesList = new ArrayList<>(list);
            if (null == mAdapter) {
                RecyclerView rv = findViewById(R.id.rv);
                rv.setVisibility(View.VISIBLE);
                mLayoutManager = new StaggeredGridLayoutManager(gridCount, StaggeredGridLayoutManager.VERTICAL);
                rv.setLayoutManager(mLayoutManager);
                mAdapter = new NotesAdapter(mNotesList, getLayoutInflater(), this);
                rv.setItemAnimator(new DefaultItemAnimator());
                rv.setAdapter(mAdapter);
                touchHelper = new SimpleTouchHelper(mAdapter);
            }
        }, 275));
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, int position, boolean alreadySelected) {
        if (isSelectionModeActive) {
            if (alreadySelected) {
                touchHelper.onItemUnselected(viewHolder, position);
                selectedItems--;
                if (selectedItems == 0) {
                    disableSelectionUi();
                    touchHelper.onDismissSelection(viewHolder, position);
                }
            } else {
                touchHelper.onItemSelected(viewHolder, position);
                selectedItems++;
            }
        } else {
            Intent i = new Intent(MainActivity.this, EditNote.class);
            i.putExtra(EditNote.NOTE_FILE_NAME, mNotesList.get(position).getFileName());
            i.putExtra(EditNote.POSITION_EDITED, position);
            startActivityForResult(i, REQUEST_CODE_EDIT_NOTE);
        }
    }

    @Override
    public boolean onStartSelection(RecyclerView.ViewHolder viewHolder, int position) {
        if (!isSelectionModeActive) {
            touchHelper.onItemSelected(viewHolder, position);
            enableSelectionUi();
            selectedItems++;
        }
        return true;
    }

    private void enableSelectionUi() {
        isSelectionModeActive = true;
        fab.setImageResource(R.drawable.ic_delete);
    }

    private void disableSelectionUi() {
        isSelectionModeActive = false;
        fab.setImageResource(R.drawable.ic_note);
    }

    private void delete() {
        List<NoteModel> deleteList = new ArrayList<>(mAdapter.getSelectedList());
        NotesHelper.deleteNotes(this, deleteList);
        touchHelper.onSelectedItemDelete();
        disableSelectionUi();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (null != data) {
                NoteModel nm = (NoteModel) data.getSerializableExtra(NOTE_MODEL);
                if (requestCode == REQUEST_CODE_CREATE_NOTE) {
                    if (null == mAdapter) loadNotes();
                    else if (null != nm) mAdapter.addItem(nm);

                } else if (requestCode == REQUEST_CODE_EDIT_NOTE) {
                    if (null == mAdapter) loadNotes();
                    else {
                        int pos = data.getIntExtra(EditNote.POSITION_EDITED, -1);
                        mAdapter.updateItem(nm, pos);
                    }
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        int newOrientation = newConfig.orientation;
        if (newOrientation == orientation)
            return;
        orientation = newOrientation;

        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridCount = GRID_COUNT_LANDSCAPE;
        } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
            gridCount = GRID_COUNT_PORTRAIT;
        }
        if (null != mLayoutManager)
            mLayoutManager.setSpanCount(gridCount);
    }

    @Override
    protected void onStart() {
        if (ThemeManagerUtils.getTheme() != mTheme)
            recreate();
        super.onStart();
    }
}
