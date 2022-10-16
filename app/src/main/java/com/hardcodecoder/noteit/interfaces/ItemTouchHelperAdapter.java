package com.hardcodecoder.noteit.interfaces;

public interface ItemTouchHelperAdapter {

    void onItemSelected(int position);

    void onItemUnselected(int position);

    void onSelectedItemDelete();

    void onSelectionDismiss();
}
