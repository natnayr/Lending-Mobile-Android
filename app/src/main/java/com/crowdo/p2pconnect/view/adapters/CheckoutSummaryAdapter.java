package com.crowdo.p2pconnect.view.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.model.others.CheckoutSummaryBid;
import com.crowdo.p2pconnect.viewholders.ItemCheckoutSummaryViewHolder;

import java.util.List;

public class CheckoutSummaryAdapter extends RecyclerView.Adapter<ItemCheckoutSummaryViewHolder>{
    private static final String LOG_TAG = CheckoutSummaryAdapter.class.getSimpleName();
    private final List<CheckoutSummaryBid> summaryList;

    public CheckoutSummaryAdapter(List<CheckoutSummaryBid> list) {
        this.summaryList = list;
    }

    @Override
    public ItemCheckoutSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout_summary, parent, false);
        return new ItemCheckoutSummaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemCheckoutSummaryViewHolder holder, int position) {

        CheckoutSummaryBid bidItem = summaryList.get(position);

        Log.d(LOG_TAG, "APP bidItem.getLoanId(): " + bidItem.getLoanId());

        holder.mItemCheckoutSummaryLoanId.setText(bidItem.getLoanId());
        holder.mItemCheckoutSummaryTenure.setText(String.valueOf(bidItem.getTenureOut()));
        holder.mItemCheckoutSummaryGrade.setText(bidItem.getGrade());
        holder.mItemCheckoutSummaryInterestRate.setText(String.valueOf(bidItem.getInterestRateOut()));
    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }

}
