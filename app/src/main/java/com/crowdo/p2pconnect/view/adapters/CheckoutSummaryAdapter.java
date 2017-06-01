package com.crowdo.p2pconnect.view.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.viewholders.ItemCheckoutSummaryViewHolder;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckoutSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    @BindString(R.string.checkout_summary_item_tenure_label) String mCheckoutSummaryItemTenureLabel;
    @BindString(R.string.checkout_summary_item_interest_rate_label) String mCheckoutSummaryItemInterestRateLabel;


    private static final String LOG_TAG = CheckoutSummaryAdapter.class.getSimpleName();
    private List<Investment> biddingInvestmentList;
    private List<Loan> biddingLoanList;
    private int totalPendingBids;
    private Context mContext;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public CheckoutSummaryAdapter(Context context) {
        this.mContext = context;
        this.biddingInvestmentList = new ArrayList<Investment>();
        this.biddingLoanList = new ArrayList<Loan>();
        this.totalPendingBids = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_checkout_summary, parent, false);
            return new HeaderCheckoutSummaryViewHolder(view);
        }else if(viewType == TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_checkout_summary, parent, false);

            return new ItemCheckoutSummaryViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HeaderCheckoutSummaryViewHolder){
            HeaderCheckoutSummaryViewHolder headerHolder = (HeaderCheckoutSummaryViewHolder) holder;
            Log.d(LOG_TAG, "APP totalPendingBids: " + totalPendingBids);

            headerHolder.mHeaderCheckoutSummaryNoOfLoans.setText(Integer.toString(totalPendingBids));

        }else if(holder instanceof ItemCheckoutSummaryViewHolder){
            ItemCheckoutSummaryViewHolder itemHolder = (ItemCheckoutSummaryViewHolder) holder;

            //taking note of header, thus position-1
            Investment bidInvestmentItem = biddingInvestmentList.get(position-1);
            Loan bidLoanItem = biddingLoanList.get(position-1);

            Log.d(LOG_TAG, "APP bidItem.getLoanId(): " + bidInvestmentItem.getLoanId());
            itemHolder.mItemCheckoutSummaryLoanId.setText(bidLoanItem.getLoanId());
            itemHolder.mItemCheckoutSummaryTenure.setText(Integer.toString(bidLoanItem.getTenure()) + mCheckoutSummaryItemTenureLabel);
            itemHolder.mItemCheckoutSummaryGrade.setText(bidLoanItem.getGrade());
            itemHolder.mItemCheckoutSummaryInterestRate.setText(Double.toString(bidLoanItem.getInterestRate()) + mCheckoutSummaryItemInterestRateLabel);
        }
    }

    @Override
    public int getItemCount() {
        //investment list should be equal to loan list + header + footer
        return biddingInvestmentList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)){
            return TYPE_HEADER;
        }else {
            return TYPE_ITEM;
        }
    }

    public void setBiddingInvestmentsAndLoans(@Nullable List<Investment> investments,
                                              @Nullable List<Loan> loans,
                                              @Nullable int totalPendingBids){
        if(investments == null || loans == null){
            return; //must not be null
        }else if(investments.size() != loans.size()){
            return; //must be equal in size
        }

        this.totalPendingBids = totalPendingBids;
        biddingInvestmentList.clear();
        biddingLoanList.clear();
        biddingInvestmentList.addAll(investments);
        biddingLoanList.addAll(loans);
        notifyDataSetChanged();
    }


    private boolean isPositionHeader (int position){
        return position == 0;
    }

//    private boolean isPositionFooter(int position){
//        return position == biddingInvestmentList.size() + 1;
//    }


    class HeaderCheckoutSummaryViewHolder extends RecyclerView.ViewHolder{
        @Nullable @BindView(R.id.checkout_summary_no_of_loans_value)
        TextView mHeaderCheckoutSummaryNoOfLoans;

        @Nullable @BindView(R.id.checkout_summary_no_of_loans_icon)
        ImageView mSummaryCartNoOfLoansIcon;

        HeaderCheckoutSummaryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mSummaryCartNoOfLoansIcon.setImageDrawable(
                    new IconicsDrawable(mContext)
                            .icon(CommunityMaterial.Icon.cmd_cash_multiple)
                            .colorRes(R.color.color_secondary_text)
                            .sizeRes(R.dimen.checkout_summary_cart_icon_size)
            );
        }
    }

}
