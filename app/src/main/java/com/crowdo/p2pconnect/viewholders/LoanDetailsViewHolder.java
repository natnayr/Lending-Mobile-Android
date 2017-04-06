package com.crowdo.p2pconnect.viewholders;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.custom_ui.GoalProgressBar;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.model.LoanDetail;
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
 * Created by cwdsg05 on 15/12/16.
 */

public class LoanDetailsViewHolder {

    private static final String LOG_TAG = LoanDetailsViewHolder.class.getSimpleName();

    private static final int AMOUNT_UNIT = 1;
    private static final int ENTER_AMOUNT_MAX_LENGTH = 4;

    // views
    @BindView(R.id.loan_details_content) RelativeLayout mLoanDetailRelativeLayout;
    @BindView(R.id.loan_detail_iden_no) TextView mLoanIdenTextView;
    @BindView(R.id.loan_detail_percentage_return) TextView mPercentageReturn;
    @BindView(R.id.loan_detail_grade) TextView mGrade;
    @BindView(R.id.loan_detail_security_icon_container) ImageView mSecurityIcon;
    @BindView(R.id.loan_detail_security_description) TextView mSecurityDescription;

    @BindView(R.id.loan_detail_progress_bar) GoalProgressBar mProgressBar;
    @BindView(R.id.loan_detail_progress_description) TextView mProgressDescription;

    @BindView(R.id.loan_detail_tenure_duration) TextView mTenureDuration;
    @BindView(R.id.loan_detail_days_left) TextView mNumDaysLeft;
    @BindView(R.id.loan_detail_target_amount) TextView mTargetAmount;
    @BindView(R.id.loan_detail_target_amount_description) TextView mTargetAmountDescription;

    @BindView(R.id.loan_detail_schedule_start_date) TextView mScheduleStartDate;
    @BindView(R.id.loan_detail_schedule_first_repayment_date) TextView mScheduleFirstRepaymentDate;
    @BindView(R.id.loan_detail_schedule_last_repayment_date) TextView mScheduleLastRepaymentDate;
    @BindView(R.id.loan_detail_avalible_amount) TextView mAvalibleAmount;

    // to interact with
    @BindView(R.id.loan_detail_amount_minus_btn) ImageButton mAmountMinusBtn;
    @BindView(R.id.loan_detail_amount_plus_btn) ImageButton mAmountPlusBtn;
    @BindView(R.id.loan_detail_enter_amount_edittext) public EditText mEnterAmount;
    @BindView(R.id.loan_detail_factsheet_download_btn) public LinearLayout mFactsheetDownloadBtn; //LinearLayout act as button
    @BindView(R.id.loan_detail_bid_enter_btn) public LinearLayout mBidEnterBtn; //LinearLayout act as button

    // String Bindings
    @BindString(R.string.loan_detail_target_amount_principal) String mTargetAmountPrincipalString;

    @BindString(R.string.loan_detail_out_sec_uncollateralized) String mOutSecUncollateralized;
    @BindString(R.string.loan_detail_out_sec_invoice_or_cheque) String mOutSecInvoiceOrCheque;
    @BindString(R.string.loan_detail_out_sec_personal_guarantee) String mOutSecPersonalGuarantee;
    @BindString(R.string.loan_detail_progress_description_tail_label) String mProgressDescriptionTail;

    @BindDrawable(R.drawable.ic_decagram_outline_black_38dp) Drawable mDecagramDrawable;

    // color
    @BindColor(R.color.color_icon_shield) int mShieldColor;
    @BindColor(R.color.color_icon_file_text) int mFileColor;
    @BindColor(R.color.color_icon_unlock_alt) int mLockOpenColor;
    @BindColor(R.color.color_icon_decagram) int mSealColor;
    @BindColor(R.color.color_icons_text) int mIconTextColor; //white
    @BindColor(R.color.color_divider) int mDividerColor;
    
    @BindColor(R.color.grade_color_A_plus) int mColorAPlus;
    @BindColor(R.color.grade_color_A) int mColorA;
    @BindColor(R.color.grade_color_B_plus) int mColorBPlus;
    @BindColor(R.color.grade_colorB) int mColorB;
    @BindColor(R.color.grade_colorC) int mColorC;
    @BindColor(R.color.grade_color_D) int mColorD;
    @BindColor(R.color.grade_color_E) int mColorE;

    @BindDrawable(R.drawable.loan_detail_plus_bid_btn_enabled) Drawable mPlusEnabledDrawable;
    @BindDrawable(R.drawable.loan_detail_plus_bid_btn_pressed) Drawable mPlusPressedDrawable;
    @BindDrawable(R.drawable.loan_detail_minus_bid_btn_enabled) Drawable mMinusEnabledDrawable;
    @BindDrawable(R.drawable.loan_detail_minus_bid_btn_pressed) Drawable mMinusPressedDrawable;

    public LoanDetailsViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void initView() {
        mAmountPlusBtn.setBackground(mPlusEnabledDrawable);
        mAmountMinusBtn.setBackground(mMinusEnabledDrawable);

        mAmountPlusBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mAmountPlusBtn.setBackground(mPlusPressedDrawable);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mAmountPlusBtn.getDrawable().setTint(mIconTextColor);
                        }
                        //round & minus value
                        addToEnterAmount(AMOUNT_UNIT);

                        return true;
                    case MotionEvent.ACTION_UP:
                        mAmountPlusBtn.setBackground(mPlusEnabledDrawable);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mAmountPlusBtn.getDrawable().setTint(mDividerColor);
                        }
                        return true;
                }
                return false;
            }
        });

        mAmountMinusBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mAmountMinusBtn.setBackground(mMinusPressedDrawable);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mAmountMinusBtn.getDrawable().setTint(mIconTextColor);
                        }
                        //round & minus value
                        addToEnterAmount(-AMOUNT_UNIT);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mAmountMinusBtn.setBackground(mMinusEnabledDrawable);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mAmountMinusBtn.getDrawable().setTint(mDividerColor);
                        }
                        return true;
                }
                return false;
            }
        });

        //toClear focus of editext
        mLoanDetailRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //map touch to edittext
                    if (mEnterAmount.isFocused()) {
                        Rect outRect = new Rect();
                        mEnterAmount.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            SoftInputHelper.hideSoftKeyboardOfView(mEnterAmount, view);
                        }
                    }
                }
                return false;
            }
        });

        //toClear focus when press keypad enter
        mEnterAmount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int key, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (key) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_NUMPAD_ENTER:
                            SoftInputHelper.hideSoftKeyboardOfView(mEnterAmount, view);
                        default:
                            break;
                    }
                }
                return false;
            }
        });


    }

    public void attachView(final LoanDetail loanDetail, final Context context) {

        if(!"".equals(loanDetail.getLoanId())) {
            mLoanIdenTextView.setText(loanDetail.getLoanId());
        }

        mPercentageReturn.setText(Double.toString(loanDetail.getInterestRate()));

        if(!"".equals(loanDetail.getGrade())) {
            mGrade.setText(loanDetail.getGrade());
        }
        
        if(!"".equals(loanDetail.getGrade())) {
            switch (loanDetail.getGrade()) {
                case "A+":
                    mGrade.setTextColor(mColorAPlus);
                    break;
                case "A":
                    mGrade.setTextColor(mColorA);
                    break;
                case "B+":
                    mGrade.setTextColor(mColorBPlus);
                    break;
                case "B":
                    mGrade.setTextColor(mColorB);
                    break;
                case "C":
                    mGrade.setTextColor(mColorC);
                    break;
                case "D":
                    mGrade.setTextColor(mColorD);
                    break;
                case "E":
                    mGrade.setTextColor(mColorE);
                    break;
                default:
            }
        }

        if(!"".equals(loanDetail.getSecurity())) {
            switch (loanDetail.getSecurity()) {
                case ConstantVariables.IN_SEC_COLLATERALIZED:
                    mSecurityIcon.setImageDrawable(new IconicsDrawable(context)
                            .icon(CommunityMaterial.Icon.cmd_shield_outline)
                            .sizeRes(R.dimen.loan_detail_security_icon_size));
                    mSecurityIcon.setColorFilter(mShieldColor);
                    String collateralDesc = WordUtils.wrap(
                            WordUtils.capitalize(loanDetail.getCollateral()
                                    + "\n" + ConstantVariables.IN_SEC_COLLATERALIZED), 25);
                    mSecurityDescription.setText(collateralDesc);
                    mSecurityIcon.setContentDescription(collateralDesc);
                    break;
                case ConstantVariables.IN_SEC_UNCOLLATERALIZED:
                    mSecurityIcon.setImageDrawable(new IconicsDrawable(context)
                            .icon(CommunityMaterial.Icon.cmd_lock_open_outline)
                            .sizeRes(R.dimen.loan_detail_security_icon_size));
                    mSecurityIcon.setColorFilter(mLockOpenColor);
                    mSecurityDescription.setText(mOutSecUncollateralized);
                    mSecurityIcon.setContentDescription(mOutSecUncollateralized);
                    break;
                case ConstantVariables.IN_SEC_INVOICE_OR_CHEQUE:
                    mSecurityIcon.setImageDrawable(new IconicsDrawable(context)
                            .icon(CommunityMaterial.Icon.cmd_file_outline)
                            .sizeRes(R.dimen.loan_detail_security_icon_size));
                    mSecurityIcon.setColorFilter(mFileColor);
                    mSecurityDescription.setText(mOutSecInvoiceOrCheque);
                    mSecurityIcon.setContentDescription(mOutSecInvoiceOrCheque);
                    break;
                case ConstantVariables.IN_SEC_PERSONAL_GUARANTEE:
                    mSecurityIcon.setImageDrawable(mDecagramDrawable);
                    mSecurityIcon.setColorFilter(mSealColor);
                    mSecurityDescription.setText(mOutSecPersonalGuarantee);
                    mSecurityIcon.setContentDescription(mOutSecPersonalGuarantee);
                    break;
                default:
            }
        }

        int progressNum = loanDetail.getFundedPercentageCache();
        mProgressBar.setProgress(progressNum);
        mProgressBar.setContentDescription(Integer.toString(progressNum)
                + mProgressDescriptionTail);
        mProgressDescription.setText(progressNum + mProgressDescriptionTail);
        mTenureDuration.setText(Integer.toString(loanDetail.getTenure()));

        if(!"".equals(loanDetail.getFundingEndDate())) {
            int daysLeft = DateUtils.findDaysLeft(ConstantVariables.DATE_TIME_REGION,
                    loanDetail.getFundingEndDate());

            if (daysLeft > 0) {
                mNumDaysLeft.setText(Integer.toString(daysLeft));
            } else {
                mNumDaysLeft.setText("0");
            }
        }


        if(!"".equals(loanDetail.getCurrency())) {
            mTargetAmount.setText(NumericUtils.truncateNumber(loanDetail.getTargetAmount(),
                    LocaleHelper.getLanguage(context)));
            mTargetAmountDescription.setText(mTargetAmountPrincipalString +
                    " (" + loanDetail.getCurrency() + ")");
        }

        if(!"".equals(loanDetail.getStartDate()))
            mScheduleStartDate.setText(DateUtils.dateTimeFormatter(
                    ConstantVariables.OUT_DATE_TIME_FORMAT, loanDetail.getStartDate()));
        if(!"".equals(loanDetail.getFirstRepayment()))
            mScheduleFirstRepaymentDate.setText(DateUtils.dateTimeFormatter(
                    ConstantVariables.OUT_DATE_TIME_FORMAT, loanDetail.getFirstRepayment()));
        if(!"".equals(loanDetail.getLastRepayment()))
            mScheduleLastRepaymentDate.setText(DateUtils.dateTimeFormatter(
                    ConstantVariables.OUT_DATE_TIME_FORMAT, loanDetail.getLastRepayment()));

        if(!"".equals(loanDetail.getCurrency()))
            mAvalibleAmount.setText(NumericUtils.formatCurrency(loanDetail.getCurrency(),
                    loanDetail.getFundingAmountToCompleteCache(), loanDetail.getCurrency()+" ", false) +
                    " " + loanDetail.getCurrency());

        //Add textwatcher here cause of required currency
        mEnterAmount.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    mEnterAmount.removeTextChangedListener(this);
                    if (!s.toString().equals(current)) {

                        //remove all non-digit
                        current = s.toString().replaceAll("[^\\d]", "").trim();

                        if(Long.parseLong(current) < Integer.MAX_VALUE) {
                            int num = Integer.parseInt(current);
                            if(num < 0){
                                current = "0";
                            }else if(num >= Math.pow(10, ENTER_AMOUNT_MAX_LENGTH)){
                                current = Double.toString(Math.pow(10, ENTER_AMOUNT_MAX_LENGTH));
                            }
                        }

                        mEnterAmount.setText(current);
                        mEnterAmount.setSelection(current.length());
                    }
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    //catch long error
                    Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    //clear it for them
                    mEnterAmount.setText(current);
                    mEnterAmount.setSelection(current.length());
                } finally {
                    mEnterAmount.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void addToEnterAmount(int byAmount) {
        String cleanString = mEnterAmount.getText().toString()
                .replaceAll("[^\\d]", "").trim();

        if ("0".equals(cleanString) || "".equals(cleanString)) {
            if (byAmount > 0) {
                //post byAmount
                mEnterAmount.setText(Integer.toString(AMOUNT_UNIT));
            }
        } else if (cleanString.length() <= ENTER_AMOUNT_MAX_LENGTH) {
            int curAmount = Integer.parseInt(cleanString);
            if ((curAmount + byAmount) >= 0 &&
                    (curAmount + byAmount) < (Math.pow(10, ENTER_AMOUNT_MAX_LENGTH))) {
                mEnterAmount.setText(Integer.toString(curAmount + byAmount));
            }
        }
    }
}
