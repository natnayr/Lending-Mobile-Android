package com.crowdo.p2pmobile.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.data.LoanListItem;
import com.crowdo.p2pmobile.helper.CustomDateHelper;
import com.crowdo.p2pmobile.helper.CustomNumberFormatter;
import com.crowdo.p2pmobile.helper.FontManager;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by cwdsg05 on 15/11/16.
 */

public class LoanListAdapter extends BaseAdapter {

    private final String LOG_TAG = LoanListAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<LoanListItem> mLoanList = new ArrayList<LoanListItem>();

    private static final String IN_FREQUENCY_MONTH_VALUE = "Monthly";
    private static final String OUT_FREQUENCY_MONTH_VALUE = "Months";

    private static final String IN_SEC_COLLATERAL = "Collateral";
    private static final String OUT_SEC_COLLATERAL = "";
    private static final String IN_SEC_UNCOLLATERALIZED = "Uncollateralized";
    private static final String OUT_SEC_UNCOLLATERALIZED = "No Collateral";
    private static final String IN_SEC_INVOICE_OR_CHEQUE = "Working Order/Invoice";
    private static final String OUT_SEC_INVOICE_OR_CHEQUE = "Working Order/\nInvoice";


    public LoanListAdapter(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mLoanList.size();
    }

    @Override
    public LoanListItem getItem(int position) {
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
        final LoanListViewHolder viewHolder = (LoanListViewHolder) view.getTag();
        viewHolder.attachLoanItem(getItem(position), mContext);
        return view;
    }

    private View createView(ViewGroup parent){
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.list_item_loan, parent, false);
        final LoanListViewHolder loanListViewHolder = new LoanListViewHolder(view);
        view.setTag(loanListViewHolder);
        return view;
    }

    public void setLoans(@Nullable List<LoanListItem> retreivedLoans){
        if(retreivedLoans == null){
            return;
        }
        mLoanList.clear();
        mLoanList.addAll(retreivedLoans);
        notifyDataSetChanged();
    }

    // LoanListViewHolder Pattern for ButterKnife
    static class LoanListViewHolder {
        @BindView(R.id.loan_item_iden_no) TextView mLoanId;
        @BindView(R.id.loan_item_credit_grade_text) TextView mLoanGrade;
        @BindView(R.id.loan_item_days_left_and_percentage_funded) TextView mDaysLeftAndPercentage;
        @BindView(R.id.loan_item_percentage_return) TextView mPercentageReturn;
        @BindView(R.id.loan_item_tenure_amount) TextView mTermAmount;
        @BindView(R.id.loan_item_tenure_description) TextView mTermDescription;
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

        @BindColor(R.color.fa_icon_shield) int shieldColor;
        @BindColor(R.color.fa_icon_file_text) int fileColor;
        @BindColor(R.color.fa_icon_unlock_alt) int unlockAltColor;

        @BindString(R.string.date_time_region) String DATE_TIME_REGION;

        public LoanListViewHolder(View view){
            ButterKnife.bind(this, view);
        }

        public void attachLoanItem(LoanListItem item, Context context){
            mLoanId.setText(item.loanId);
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


            int daysLeft = CustomDateHelper.findDaysLeft(DATE_TIME_REGION, item.fundingEndDate);

            if(daysLeft<0){
                mDaysLeftAndPercentage.setText("Closed - " + item.fundedPercentageCache + "% Funded");
            }else{
                mDaysLeftAndPercentage.setText(daysLeft + " Days Left - " + item.fundedPercentageCache + "% Funded");
            }

            mPercentageReturn.setText(Double.toString(item.interestRate));
            mTermAmount.setText(Integer.toString(item.tenure));

            //check if monthly is set differently
            String termDescription = OUT_FREQUENCY_MONTH_VALUE;
            if(!item.frequency.equals(IN_FREQUENCY_MONTH_VALUE))
                termDescription = item.frequency;
            mTermDescription.setText(termDescription);

            switch(item.security){
                case IN_SEC_COLLATERAL:
                    mSecurityIcon.setText(R.string.fa_shield);
                    mSecurityIcon.setTextColor(shieldColor);
                    mSecurityDescription.setText(WordUtils.wrap(
                        WordUtils.capitalize(item.collateral.replaceAll("_", " ")), 15));
                    break;
                case IN_SEC_UNCOLLATERALIZED:
                    mSecurityIcon.setText(R.string.fa_unlock_alt);
                    mSecurityIcon.setTextColor(unlockAltColor);
                    mSecurityDescription.setText(OUT_SEC_UNCOLLATERALIZED);
                    break;
                case IN_SEC_INVOICE_OR_CHEQUE:
                    mSecurityIcon.setText(R.string.fa_file_text);
                    mSecurityIcon.setTextColor(fileColor);
                    mSecurityDescription.setText(OUT_SEC_INVOICE_OR_CHEQUE);
                    break;
            }

            mLoanAmount.setText(CustomNumberFormatter.formatCurrency(item.currency,
                    item.targetAmount, item.currency+" ", true));

            Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
            FontManager.markAsIconContainer(mSecurityIcon, iconFont);
            FontManager.markAsIconContainer(mLoanAmountIcon, iconFont);
        }
    }
}
