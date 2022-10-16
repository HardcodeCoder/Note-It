package com.hardcodecoder.noteit.adapters;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hardcodecoder.noteit.R;
import com.hardcodecoder.noteit.helper.DiffCb;
import com.hardcodecoder.noteit.interfaces.ItemClickListener;
import com.hardcodecoder.noteit.interfaces.ItemTouchHelperAdapter;
import com.hardcodecoder.noteit.interfaces.ItemTouchHelperViewHolder;
import com.hardcodecoder.noteit.models.NoteModel;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> implements ItemTouchHelperAdapter {

    private final SparseBooleanArray mSelectionState = new SparseBooleanArray();
    private List<NoteModel> mNotesList;
    private LayoutInflater mInflater;
    private ItemClickListener mListener;
    private List<NoteModel> mSelectedItem = new ArrayList<>();

    public NotesAdapter(List<NoteModel> mNotesList, LayoutInflater mInflater, ItemClickListener mListener) {
        this.mNotesList = mNotesList;
        this.mInflater = mInflater;
        this.mListener = mListener;
    }

    public void addItem(NoteModel noteModel) {
        mNotesList.add(0, noteModel);
        notifyItemInserted(0);
    }

    public void updateItem(NoteModel noteModel, int position) {
        if (null == noteModel) {
            mNotesList.remove(position);
            notifyItemRemoved(position);
        } else {
            mNotesList.remove(position);
            mNotesList.add(position, noteModel);
            notifyItemChanged(position);
        }
    }

    public List<NoteModel> getSelectedList() {
        return mSelectedItem;
    }

    private void dispatchChanges(List<NoteModel> list) {
        DiffUtil.Callback callback = new DiffCb(mNotesList, list);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        mNotesList.clear();
        mNotesList.addAll(list);
        result.dispatchUpdatesTo(this);
    }

    @Override
    public void onItemSelected(int position) {
        mSelectedItem.add(mNotesList.get(position));
        mSelectionState.put(position, true);
    }

    @Override
    public void onItemUnselected(int position) {
        mSelectedItem.remove(mNotesList.get(position));
        mSelectionState.put(position, false);
    }

    @Override
    public void onSelectedItemDelete() {
        mSelectionState.clear();
        List<NoteModel> temp = new ArrayList<>(mNotesList);
        temp.removeAll(mSelectedItem);
        mSelectedItem.clear();
        dispatchChanges(temp);
    }

    @Override
    public void onSelectionDismiss() {
        mSelectedItem.clear();
        mSelectionState.clear();
    }


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(mInflater.inflate(R.layout.rv_note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        final boolean isSelected = mSelectionState.get(position, false);

        // {parameter: int position} may not be valid after a DiffUtil.DiffResult#dispatchChangesTo(this)
        // call so get the position from view holders adapter position
        holder.itemView.setOnClickListener(v -> mListener.onItemClick(holder, holder.getAdapterPosition(), isSelected));
        holder.itemView.setOnLongClickListener(v -> mListener.onStartSelection(holder, holder.getAdapterPosition()));

        if (isSelected)
            holder.itemView.setBackground(holder.itemView.getContext().getDrawable(R.drawable.selected_item_background));
        else
            holder.itemView.setBackground(holder.itemView.getContext().getDrawable(R.drawable.note_item_border));

        holder.updateData(mNotesList.get(position));
    }

    @Override
    public int getItemCount() {
        if (null == mNotesList)
            return 0;
        return mNotesList.size();
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        private TextView mTitle, mNotes;

        NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.note_title);
            mNotes = itemView.findViewById(R.id.note_notes);
        }

        void updateData(NoteModel nm) {
            mTitle.setText(nm.getNoteTextModel().getTitle());
            mNotes.setText(nm.getNoteTextModel().getNotes());
        }

        @Override
        public void onItemSelected() {
            itemView.setBackground(itemView.getContext().getDrawable(R.drawable.selected_item_background));
        }

        @Override
        public void onItemClear() {
            itemView.setBackground(itemView.getContext().getDrawable(R.drawable.note_item_border));
        }
    }
}
