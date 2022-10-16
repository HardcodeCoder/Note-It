package com.hardcodecoder.noteit.helper;

import androidx.recyclerview.widget.DiffUtil;

import com.hardcodecoder.noteit.models.NoteModel;

import java.util.List;

public class DiffCb extends DiffUtil.Callback {

    private List<NoteModel> oldItems;
    private List<NoteModel> newItems;

    public DiffCb(List<NoteModel> oldItems, List<NoteModel> newItems) {
        this.oldItems = oldItems;
        this.newItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition));

    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).getFileName().equals(newItems.get(newItemPosition).getFileName());
    }
}
