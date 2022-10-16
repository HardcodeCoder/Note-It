package com.hardcodecoder.noteit.helper;

import androidx.recyclerview.widget.RecyclerView;

import com.hardcodecoder.noteit.interfaces.ItemTouchHelperAdapter;
import com.hardcodecoder.noteit.interfaces.ItemTouchHelperViewHolder;

public class SimpleTouchHelper {

    private ItemTouchHelperAdapter mAdapter;

    public SimpleTouchHelper(ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public void onItemSelected(RecyclerView.ViewHolder viewHolder, int position) {
        mAdapter.onItemSelected(position);
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            // Let the view holder know that this item is being selected
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemSelected();
        }
    }

    public void onItemUnselected(RecyclerView.ViewHolder viewHolder, int position) {
        mAdapter.onItemUnselected(position);
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            // Let the view holder know that this item is being unselected
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }

    public void onSelectedItemDelete() {
        mAdapter.onSelectedItemDelete();
    }

    public void onDismissSelection(RecyclerView.ViewHolder viewHolder, int position) {
        mAdapter.onSelectionDismiss();
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            // Let the view holder know that this item is being unselected
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
}
