package com.crowdo.p2pmobile.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.data.LoanListItem;
import com.crowdo.p2pmobile.helper.DateUtils;
import com.crowdo.p2pmobile.helper.NumericUtils;
import com.crowdo.p2pmobile.helper.FontUtils;

import org.apache.commons.lang3.text.WordUtils;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 6/1/17.
 */

public class LoanListViewHolder {

    private static final String IN_FREQUENCY_MONTH_VALUE = "Monthly";
    private static final String OUT_FREQUENCY_MONTH_VALUE = "Months";

    private static final String IN_SEC_COLLATERAL = "Collateral";
    private static final String OUT_SEC_COLLATERAL = "";
    private static final String IN_SEC_UNCOLLATERALIZED = "Uncollateralized";
    private static final String OUT_SEC_UNCOLLATERALIZED = "No Collateral";
    private static final String IN_SEC_INVOICE_OR_CHEQUE = "Working Order/Invoice";
    private static final String OUT_SEC_INVOICE_OR_CHEQUE = "Working Order/\nInvoice";

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

    public LoanListViewHolder(Context mContext, View view){
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

        int daysLeft = DateUtils.findDaysLeft(DATE_TIME_REGION, item.fundingEndDate);

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

        mLoanAmount.setText(NumericUtils.formatCurrency(item.currency,
                item.targetAmount, item.currency+" ", true));

        Typeface iconFont = FontUtils.getTypeface(context, FontUtils.FONTAWESOME);
        FontUtils.markAsIconContainer(mSecurityIcon, iconFont);
        FontUtils.markAsIconContainer(mLoanAmountIcon, iconFont);
    }
}
