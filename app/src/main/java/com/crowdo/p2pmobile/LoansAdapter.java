package com.crowdo.p2pmobile;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crowdo.p2pmobile.data.LoanItem;
import com.crowdo.p2pmobile.helper.FontManager;


import org.apache.commons.lang3.text.WordUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by cwdsg05 on 15/11/16.
 */

public class LoansAdapter extends BaseAdapter {

    private final String LOG_TAG = LoansAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<LoanItem> mLoanList = new ArrayList<LoanItem>();

    private static final String IN_FREQUENCY_MONTH_VALUE = "Monthly";
    private static final String OUT_FREQUENCY_MONTH_VALUE = "Months";

    private static final String IN_SEC_COLLATERAL = "Collateral";
    private static final String OUT_SEC_COLLATERAL = "";
    private static final String IN_SEC_UNCOLLATERALIZED = "Uncollateralized";
    private static final String OUT_SEC_UNCOLLATERALIZED = "No Collateral";
    private static final String IN_SEC_INVOICE_OR_CHEQUE = "Working Order/Invoice";
    private static final String OUT_SEC_INVOICE_OR_CHEQUE = "Working Order/\nInvoice";


    public LoansAdapter(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mLoanList.size();
    }

    @Override
    public LoanItem getItem(int position) {
        if(position < 0 || position >= mLoanList.size()){
            return null;
        }else{
            return mLoanList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View contextView, ViewGroup parent) {
        final View view = (contextView != null ? contextView : createView(parent));
        final LoanViewHolder viewHolder = (LoanViewHolder) view.getTag();
        viewHolder.attachLoanItem(getItem(position), mContext);
        return view;
    }

    private View createView(ViewGroup parent){
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.list_item_loan, parent, false);
        final LoanViewHolder loanViewHolder = new LoanViewHolder(view);
        view.setTag(loanViewHolder);
        return view;
    }

    public void setLoans(@Nullable List<LoanItem> retreivedLoans){
        if(retreivedLoans == null){
            return;
        }
        mLoanList.clear();
        mLoanList.addAll(retreivedLoans);
        notifyDataSetChanged();
        Log.d(LOG_TAG, "TEST: setmLoanList clear and addAll");
    }

    // LoanViewHolder Pattern for ButterKnife
    static class LoanViewHolder {
        @BindView(R.id.loan_item_iden_no) TextView mLoanId;
        @BindView(R.id.loan_item_credit_grade_text) TextView mLoanGrade;
        @BindView(R.id.loan_item_days_left_and_percentage_funded) TextView mDaysLeftAndPercentage;
        @BindView(R.id.loan_item_percentage_return) TextView mPercentageReturn;
        @BindView(R.id.loan_item_term_amount) TextView mTermAmount;
        @BindView(R.id.loan_item_term_description) TextView mTermDescription;
        @BindView(R.id.loan_item_collateral_description) TextView mSecurityDescription;
        @BindView(R.id.loan_item_amount) TextView mLoanAmount;

        @BindView(R.id.loan_item_collateral_icon_container) TextView mSecurityIcon;

        @BindView(R.id.loan_item_credit_grade_layout) View mLoanGradeDrawable;
        @BindView(R.id.loan_item_amount_icon_container) TextView mLoanAmountIcon;

        @BindColor(R.color.grade_color_A_plus) int colorAPlus;
        @BindColor(R.color.grade_color_A) int colorA;
        @BindColor(R.color.grade_color_B_plus) int colorBPlus;
        @BindColor(R.color.grade_color_E) int colorB;
        @BindColor(R.color.grade_colorC) int colorC;
        @BindColor(R.color.grade_color_D) int colorD;
        @BindColor(R.color.grade_color_E) int colorE;

        public LoanViewHolder(View view){
            ButterKnife.bind(this, view);
        }

        public void attachLoanItem(LoanItem item, Context context){
            mLoanId.setText(item.loanIdOut);
            mLoanGrade.setText(item.grade);

            GradientDrawable mGradeShape = (GradientDrawable) mLoanGradeDrawable.getBackground();
            switch (item.grade) {
                case "A+": mGradeShape.setColor(colorAPlus);
                    break;
                case "A": mGradeShape.setColor(colorA);
                    break;
                case "B+": mGradeShape.setColor(colorBPlus);
                    break;
                case "B": mGradeShape.setColor(colorB);
                    break;
                case "C": mGradeShape.setColor(colorC);
                    break;
                case "D": mGradeShape.setColor(colorD);
                    break;
                case "E": mGradeShape.setColor(colorE);
                    break;
            }

            DateTime endDate = new DateTime(item.fundingEndDate);
            int daysLeft = Days.daysBetween(DateTime.now().toLocalDate(), endDate.toLocalDate()).getDays();

            if(daysLeft<0){
                mDaysLeftAndPercentage.setText("Closed - " + item.fundedPercentageCache + "% Funded");
            }else{
                mDaysLeftAndPercentage.setText(daysLeft + " Days Left - " + item.fundedPercentageCache + "% Funded");
            }

            mPercentageReturn.setText(Double.toString(item.interestRateOut));
            mTermAmount.setText(Integer.toString(item.tenureOut));

            //check if monthly is set differently
            String termDescription = OUT_FREQUENCY_MONTH_VALUE;
            if(!item.frequencyOut.equals(IN_FREQUENCY_MONTH_VALUE))
                termDescription = item.frequencyOut;
            mTermDescription.setText(termDescription);

            switch(item.security){
                case IN_SEC_COLLATERAL:
                    mSecurityIcon.setText(R.string.fa_shield);
                    mSecurityDescription.setText(WordUtils.wrap(
                        WordUtils.capitalize(item.collateralOut.replaceAll("_", " ")), 15));
                    break;
                case IN_SEC_UNCOLLATERALIZED:
                    mSecurityIcon.setText(R.string.fa_unlock_alt);
                    mSecurityDescription.setText(OUT_SEC_UNCOLLATERALIZED);
                    break;
                case IN_SEC_INVOICE_OR_CHEQUE:
                    mSecurityIcon.setText(R.string.fa_file_text);
                    mSecurityDescription.setText(OUT_SEC_INVOICE_OR_CHEQUE);
                    break;
            }

            mLoanAmount.setText(item.currencyOut + " " + (int) item.targetAmountOut);

            Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
            FontManager.markAsIconContainer(mSecurityIcon, iconFont);
            FontManager.markAsIconContainer(mLoanAmountIcon, iconFont);
        }
    }
}
