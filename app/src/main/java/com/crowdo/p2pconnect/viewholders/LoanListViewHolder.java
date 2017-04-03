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
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

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
    @BindColor(R.color.color_accent) int colorAccent;

    @BindColor(R.color.color_icon_shield) int mShieldColor;
    @BindColor(R.color.color_icon_file_text) int mFileColor;
    @BindColor(R.color.color_icon_unlock_alt) int mLockOpenColor;
    @BindColor(R.color.color_icon_decagram) int mSealColor;
    @BindColor(R.color.color_divider) int mDividerColor;

    @BindDrawable(R.drawable.ic_decagram_outline_black_38dp) Drawable mDecagramDrawable;

    @BindString(R.string.loan_list_bid_status_closed) String mBidStatusClosedLabel;
    @BindString(R.string.loan_list_bid_status_open) String mBidStatusOpenDaysLabel;
    @BindString(R.string.percent_label) String mPercentageLabel;
    @BindString(R.string.loan_list_out_sec_uncollateralized) String mOutSecurityUncollateralizedLabel;
    @BindString(R.string.loan_list_out_sec_invoice_or_cheque) String mOutSecurityInvoiceOrChequeLabel;
    @BindString(R.string.loan_list_out_sec_personal_guarantee) String mOutSecurityPersonalGuarantee;

    public LoanListViewHolder(Context mContext, View view){
        ButterKnife.bind(this, view);
    }

    public void attachLoanItem(LoanListItem item, Context context){
        mLoanId.setText(item.getLoanId());
        mLoanGrade.setText(item.getGrade());

        GradientDrawable mGradeShape = (GradientDrawable) mLoanGradeDrawable.getBackground();

        switch (item.getGrade()) {
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
            default: mGradeShape.setColor(colorAccent);
                break;
        }

        int daysLeft = DateUtils.findDaysLeft(ConstantVariables.DATE_TIME_REGION, item.getFundingEndDate());

        if(daysLeft<0){
            mDaysLeftAndPercentage.setText(mBidStatusClosedLabel +
                    Integer.toString(item.getFundedPercentageCache()) +
                    mPercentageLabel);
        }else{
            mDaysLeftAndPercentage.setText(daysLeft + mBidStatusOpenDaysLabel +
                    Integer.toString(item.getFundedPercentageCache()) +
                    mPercentageLabel);
        }

        mPercentageReturn.setText(Double.toString(item.getInterestRate()));
        mTermAmount.setText(Integer.toString(item.getTenure()));

        switch(item.getSecurity()){
            case ConstantVariables.IN_SEC_COLLATERALIZED:
                mSecurityIcon.setImageDrawable(new IconicsDrawable(context)
                        .icon(CommunityMaterial.Icon.cmd_shield_outline)
                        .sizeRes(R.dimen.loan_detail_security_icon_size));
                mSecurityIcon.setColorFilter(mShieldColor);
                String collateralDesc = WordUtils.wrap(
                        WordUtils.capitalize(item.getCollateral()), 15);
                mSecurityDescription.setText(collateralDesc);
                mSecurityIcon.setContentDescription(collateralDesc);
                break;
            case ConstantVariables.IN_SEC_UNCOLLATERALIZED:
                mSecurityIcon.setImageDrawable(new IconicsDrawable(context)
                        .icon(CommunityMaterial.Icon.cmd_lock_open)
                        .sizeRes(R.dimen.loan_detail_security_icon_size));
                mSecurityIcon.setColorFilter(mLockOpenColor);
                mSecurityDescription.setText(
                        mOutSecurityUncollateralizedLabel);
                mSecurityIcon.setContentDescription(
                        mOutSecurityUncollateralizedLabel);
                break;
            case ConstantVariables.IN_SEC_INVOICE_OR_CHEQUE:
                mSecurityIcon.setImageDrawable(new IconicsDrawable(context)
                        .icon(CommunityMaterial.Icon.cmd_file_outline)
                        .sizeRes(R.dimen.loan_detail_security_icon_size));
                mSecurityIcon.setColorFilter(mFileColor);
                mSecurityDescription.setText(
                        mOutSecurityInvoiceOrChequeLabel);
                mSecurityIcon.setContentDescription(
                        mOutSecurityInvoiceOrChequeLabel);
                break;
            case ConstantVariables.IN_SEC_PERSONAL_GUARANTEE:
                mSecurityIcon.setImageDrawable(mDecagramDrawable);
                mSecurityIcon.setColorFilter(mSealColor);
                mSecurityDescription.setText(
                        mOutSecurityPersonalGuarantee);
                mSecurityIcon.setContentDescription(
                        mOutSecurityPersonalGuarantee);
                break;
            default:
        }

        mLoanAmount.setText(NumericUtils.formatCurrency(item.getCurrency(),
                item.getTargetAmount(), item.getCurrency()+" ", true));
    }
}
