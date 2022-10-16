package com.hardcodecoder.noteit.interfaces;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemClickListener {

    void onItemClick(RecyclerView.ViewHolder viewHolder, int position, boolean alreadySelected);

    boolean onStartSelection(RecyclerView.ViewHolder viewHolder, int position);
}
