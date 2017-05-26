package com.crowdo.p2pconnect.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.model.others.CheckoutSummaryBid;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckoutSummaryAdapter extends RecyclerView.Adapter<CheckoutSummaryAdapter.ViewHolder>{
    private static final String LOG_TAG = CheckoutSummaryAdapter.class.getSimpleName();
    private final List<CheckoutSummaryBid> summaryList;

    public CheckoutSummaryAdapter(List<CheckoutSummaryBid> list) {
        this.summaryList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout_summary, parent, false);
        return new ViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CheckoutSummaryBid bidItem = summaryList.get(position);
        holder.setCheckoutSummaryBid(bidItem);

        Log.d(LOG_TAG, "APP bidItem.getLoanId(): " + bidItem.getLoanId());

        holder.mItemCheckoutSummaryLoanId.setText(bidItem.getLoanId());
        holder.mItemCheckoutSummaryTenure.setText(String.valueOf(bidItem.getTenureOut()));
    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mItemCheckoutSummaryLoanId;
        public TextView mItemCheckoutSummaryTenure;
        public TextView mItemCheckoutSummaryGrade;
        public TextView mItemCheckoutSummaryInterestRate;

        public ImageButton mItemCheckoutSummaryMinusButton;

        private CheckoutSummaryBid bidItem;
        private Context mContext;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            this.mContext = context;

            this.mItemCheckoutSummaryLoanId = (TextView) itemView.findViewById(R.id.item_checkout_summary_loanid);
            this.mItemCheckoutSummaryTenure = (TextView) itemView.findViewById(R.id.item_checkout_summary_tenure);
            this.mItemCheckoutSummaryGrade = (TextView) itemView.findViewById(R.id.item_checkout_summary_grade_value);
            this.mItemCheckoutSummaryInterestRate = (TextView) itemView.findViewById(R.id.item_checkout_summary_item_interest_rate);
        }

        public void setCheckoutSummaryBid(CheckoutSummaryBid bidItem){
            this.bidItem = bidItem;
        }


    }

}
