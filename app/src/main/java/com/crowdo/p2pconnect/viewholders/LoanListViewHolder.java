package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.model.LoanListItem;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.DateUtils;
import com.crowdo.p2pconnect.helpers.NumericUtils;

import org.apache.commons.lang3.text.WordUtils;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 6/1/17.
 */

public class LoanListViewHolder {

    @BindView(R.id.loan_item_iden_no) TextView mLoanId;
    @BindView(R.id.loan_item_credit_grade_text) TextView mLoanGrade;
    @BindView(R.id.loan_item_days_left_and_percentage_funded) TextView mDaysLeftAndPercentage;
    @BindView(R.id.loan_item_percentage_return) TextView mPercentageReturn;
    @BindView(R.id.loan_item_tenure_amount) TextView mTermAmount;
    @BindView(R.id.loan_item_collateral_description) TextView mSecurityDescription;
    @BindView(R.id.loan_item_amount) TextView mLoanAmount;

    @BindView(R.id.loan_item_collateral_icon_container) ImageView mSecurityIcon;

    @BindView(R.id.loan_item_credit_grade_layout) View mLoanGradeDrawable;

    @BindColor(R.color.grade_color_A_plus) int colorAPlus;
    @BindColor(R.color.grade_color_A) int colorA;
    @BindColor(R.color.grade_color_B_plus) int colorBPlus;
    @BindColor(R.color.grade_colorB) int colorB;
    @BindColor(R.color.grade_colorC) int colorC;
    @BindColor(R.color.grade_color_D) int colorD;
    @BindColor(R.color.grade_color_E) int colorE;

    @BindColor(R.color.fa_icon_shield) int shieldColor;
    @BindColor(R.color.fa_icon_file_text) int fileColor;
    @BindColor(R.color.fa_icon_unlock_alt) int unlockAltColor;

    @BindDrawable(R.drawable.ic_file_document_black_38dp) Drawable mFileIcon;
    @BindDrawable(R.drawable.ic_lock_open_black_38dp) Drawable mLockOpenIcon;
    @BindDrawable(R.drawable.ic_shield_outline_black_38dp) Drawable mShieldOutlineIcon;

    @BindString(R.string.date_time_region) String DATE_TIME_REGION;
    @BindString(R.string.loan_list_bid_status_closed) String BID_STATUS_CLOSED;
    @BindString(R.string.loan_list_bid_status_open) String BID_STATUS_OPEN_DAYS;
    @BindString(R.string.percent_label) String PERCENTAGE_LABEL;
    @BindString(R.string.loan_list_out_sec_uncollateralized) String OUT_SEC_UNCOLLATERALIZED;
    @BindString(R.string.loan_list_out_sec_invoice_or_cheque) String OUT_SEC_INVOICE_OR_CHEQUE;


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
            mDaysLeftAndPercentage.setText(BID_STATUS_CLOSED +
                    item.fundedPercentageCache +
                    PERCENTAGE_LABEL);
        }else{
            mDaysLeftAndPercentage.setText(daysLeft + BID_STATUS_OPEN_DAYS +
                    item.fundedPercentageCache +
                    PERCENTAGE_LABEL);
        }

        mPercentageReturn.setText(Double.toString(item.interestRate));
        mTermAmount.setText(Integer.toString(item.tenure));


        switch(item.security){
            case ConstantVariables.IN_SEC_COLLATERALIZED:
                mSecurityIcon.setImageDrawable(mShieldOutlineIcon);
                mSecurityIcon.setColorFilter(shieldColor);
                String collateralDesc = WordUtils.wrap(
                        WordUtils.capitalize(item.collateral.replaceAll("_", " ")), 15);
                mSecurityDescription.setText(collateralDesc);
                mSecurityIcon.setContentDescription(collateralDesc);
                break;
            case ConstantVariables.IN_SEC_UNCOLLATERALIZED:
                mSecurityIcon.setImageDrawable(mLockOpenIcon);
                mSecurityIcon.setColorFilter(unlockAltColor);
                mSecurityDescription.setText(
                        OUT_SEC_UNCOLLATERALIZED);
                mSecurityIcon.setContentDescription(
                        OUT_SEC_UNCOLLATERALIZED);
                break;
            case ConstantVariables.IN_SEC_INVOICE_OR_CHEQUE:
                mSecurityIcon.setImageDrawable(mFileIcon);
                mSecurityIcon.setColorFilter(fileColor);
                mSecurityDescription.setText(
                        OUT_SEC_INVOICE_OR_CHEQUE);
                mSecurityIcon.setContentDescription(
                        OUT_SEC_INVOICE_OR_CHEQUE);
                break;
        }

        mLoanAmount.setText(NumericUtils.formatCurrency(item.currency,
                item.targetAmount, item.currency+" ", true));
    }
}
