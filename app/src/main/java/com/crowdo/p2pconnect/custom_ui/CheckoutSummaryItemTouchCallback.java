package com.crowdo.p2pconnect.custom_ui;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.crowdo.p2pconnect.viewholders.ItemCheckoutSummaryViewHolder;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

/**
 * Created by cwdsg05 on 16/6/17.
 */

public class CheckoutSummaryItemTouchCallback extends ItemTouchHelperExtension.Callback{

    private Context mContext;

    public CheckoutSummaryItemTouchCallback(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(viewHolder instanceof ItemCheckoutSummaryViewHolder && isCurrentlyActive) {
            ItemCheckoutSummaryViewHolder holder = (ItemCheckoutSummaryViewHolder) viewHolder;
            if (dX < -holder.mItemDeleteBtn.getWidth()) {
                dX = -holder.mItemDeleteBtn.getWidth();
            }
            holder.mItemContainer.setTranslationX(dX);
            return;
        }
    }

}


