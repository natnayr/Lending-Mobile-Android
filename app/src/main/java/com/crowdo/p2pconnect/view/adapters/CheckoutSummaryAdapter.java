package com.crowdo.p2pconnect.view.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.viewholders.ItemCheckoutSummaryViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CheckoutSummaryAdapter extends RecyclerView.Adapter<ItemCheckoutSummaryViewHolder>{
    private static final String LOG_TAG = CheckoutSummaryAdapter.class.getSimpleName();
    private List<Investment> biddingInvestmentList;
    private List<Loan> biddingLoanList;
    private Context mContext;

    public CheckoutSummaryAdapter(Context context) {
        this.mContext = context;
        this.biddingInvestmentList = new ArrayList<Investment>();
        this.biddingLoanList = new ArrayList<Loan>();
    }

    @Override
    public ItemCheckoutSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout_summary, parent, false);
        return new ItemCheckoutSummaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemCheckoutSummaryViewHolder holder, int position) {

        Investment bidInvestmentItem = biddingInvestmentList.get(position);
        Loan bidLoanItem = biddingLoanList.get(position);
        Log.d(LOG_TAG, "APP bidItem.getLoanId(): " + bidInvestmentItem.getLoanId());

        holder.mItemCheckoutSummaryLoanId.setText(bidLoanItem.getLoanId());
        holder.mItemCheckoutSummaryTenure.setText(Integer.toString(bidLoanItem.getTenure()));
        holder.mItemCheckoutSummaryGrade.setText(bidLoanItem.getGrade());
        holder.mItemCheckoutSummaryInterestRate.setText(Double.toString(bidLoanItem.getInterestRate()));
    }

    @Override
    public int getItemCount() {
        //investment list should be equal to loan list
        return biddingInvestmentList.size();
    }

    public void setBiddingInvestmentsAndLoans(@Nullable List<Investment> investments,
                                              @Nullable List<Loan> loans){
        if(investments == null || loans == null){
            return; //must not be null
        }else if(investments.size() != loans.size()){
            return; //must be equal in size
        }

        biddingInvestmentList.clear();
        biddingLoanList.clear();
        biddingInvestmentList.addAll(investments);
        biddingLoanList.addAll(loans);
        notifyDataSetChanged();
    }

}
