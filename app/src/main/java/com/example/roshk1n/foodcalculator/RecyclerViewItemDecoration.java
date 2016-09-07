package com.example.roshk1n.foodcalculator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private int space = 0;
    private int item_count = 0;
    private int ADDITIONAL_PADDING = 20;

    public RecyclerViewItemDecoration(int space, int item_count) {
        this.space = space;
        this.item_count = item_count;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildPosition(view) != item_count)
            outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if(parent.getChildPosition(view) == 0)
            outRect.top = space + ADDITIONAL_PADDING;
        if(parent.getChildPosition(view) == item_count)
            outRect.bottom = space + ADDITIONAL_PADDING;
    }
}
