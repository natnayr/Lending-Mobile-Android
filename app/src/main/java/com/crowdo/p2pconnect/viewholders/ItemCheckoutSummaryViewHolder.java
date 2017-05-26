package com.crowdo.p2pconnect.viewholders;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 26/5/17.
 */

public class ItemCheckoutSummaryViewHolder extends RecyclerView.ViewHolder{

    @Nullable @BindView(R.id.item_checkout_summary_loanid)
    public TextView mItemCheckoutSummaryLoanId;

    @Nullable @BindView(R.id.item_checkout_summary_tenure)
    public TextView mItemCheckoutSummaryTenure;

    @Nullable @BindView(R.id.item_checkout_summary_grade_value)
    public TextView mItemCheckoutSummaryGrade;

    @Nullable @BindView(R.id.item_checkout_summary_item_interest_rate)
    public TextView mItemCheckoutSummaryInterestRate;

    public ItemCheckoutSummaryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }




}
