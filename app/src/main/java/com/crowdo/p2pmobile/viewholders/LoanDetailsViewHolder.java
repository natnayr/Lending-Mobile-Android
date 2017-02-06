package com.crowdo.p2pmobile.viewholders;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.custom_ui.GoalProgressBar;
import com.crowdo.p2pmobile.data.LoanDetail;
import com.crowdo.p2pmobile.helpers.DateUtils;
import com.crowdo.p2pmobile.helpers.NumericUtils;

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

    private static final String IN_FREQUENCY_MONTH_VALUE = "Monthly";
    private static final String OUT_FREQUENCY_MONTH_LABEL = "Months Tenure";
    private static final String OUT_FREQUENCY_MONTH_SCHEDULE_LABEL = "Monthly";

    private static final String IN_SEC_COLLATERAL = "Collateral";
    private static final String OUT_SEC_COLLATERAL = "";
    private static final String IN_SEC_UNCOLLATERALIZED = "Uncollateralized";
    private static final String OUT_SEC_UNCOLLATERALIZED = "No Collateral";
    private static final String IN_SEC_INVOICE_OR_CHEQUE = "Working Order/Invoice";
    private static final String OUT_SEC_INVOICE_OR_CHEQUE = "Working Order/\nInvoice";

    private static final String IN_DATE_TIME_FORMAT = "";
    private static final String OUT_DATE_TIME_FORMAT = "dd MMM yyyy";
    private static final int AMOUNT_UNIT = 1;
    private static final int ENTER_AMOUNT_MAX_LENGTH = 4;

    // static views
    @BindView(R.id.loan_details_content) RelativeLayout mLoanDetailRelativeLayout;
    @BindView(R.id.loan_detail_iden_no) TextView mLoanIdenTextView;
    @BindView(R.id.loan_detail_percentage_return) TextView mPercentageReturn;
    @BindView(R.id.loan_detail_grade) TextView mGrade;
    @BindView(R.id.loan_detail_security_icon_container) ImageView mSecurityIcon;
    @BindView(R.id.loan_detail_security_description) TextView mSecurityDescription;

    @BindView(R.id.loan_detail_progress_bar) GoalProgressBar mProgressBar;
    @BindView(R.id.loan_detail_progress_description) TextView mProgressDescription;

    @BindView(R.id.loan_detail_tenure_duration) TextView mTenureDuration;
    @BindView(R.id.loan_detail_tenure_description) TextView mTenureDescription;
    @BindView(R.id.loan_detail_days_left) TextView mNumDaysLeft;
    @BindView(R.id.loan_detail_target_amount) TextView mTargetAmount;
    @BindView(R.id.loan_detail_target_amount_description) TextView mTargetAmountDescription;

    @BindView(R.id.loan_detail_schedule_header) TextView mScheduleFrequencyLabel;
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
    @BindString(R.string.date_time_region) String DATE_TIME_REGION; //constant
    @BindString(R.string.loan_detail_target_amount_principal) String mTargetAmountPrincipalString;

    // color
    @BindColor(R.color.fa_icon_shield) int shieldColor;
    @BindColor(R.color.fa_icon_file_text) int fileColor;
    @BindColor(R.color.fa_icon_unlock_alt) int lockOpenColor;
    @BindColor(R.color.color_icons_text) int iconTextColor; //white
    @BindColor(R.color.color_divider) int dividerColor;
    
    @BindColor(R.color.grade_color_A_plus) int colorAPlus;
    @BindColor(R.color.grade_color_A) int colorA;
    @BindColor(R.color.grade_color_B_plus) int colorBPlus;
    @BindColor(R.color.grade_colorB) int colorB;
    @BindColor(R.color.grade_colorC) int colorC;
    @BindColor(R.color.grade_color_D) int colorD;
    @BindColor(R.color.grade_color_E) int colorE;

    //drawables extras
    @BindDrawable(R.drawable.ic_file_document_black_38dp) Drawable mFileIcon;
    @BindDrawable(R.drawable.ic_lock_open_black_38dp) Drawable mLockOpenIcon;
    @BindDrawable(R.drawable.ic_shield_outline_black_38dp) Drawable mShieldOutlineIcon;

    @BindDrawable(R.drawable.loan_detail_plus_bid_btn_enabled) Drawable mPlusEnabledDrawable;
    @BindDrawable(R.drawable.loan_detail_plus_bid_btn_pressed) Drawable mPlusPressedDrawable;
    @BindDrawable(R.drawable.loan_detail_minus_bid_btn_enabled) Drawable mMinusEnabledDrawable;
    @BindDrawable(R.drawable.loan_detail_minus_bid_btn_pressed) Drawable mMinusPressedDrawable;

    public LoanDetailsViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void initView(final Context context, final int holderId) {

        mAmountPlusBtn.setBackground(mPlusEnabledDrawable);
        mAmountMinusBtn.setBackground(mMinusEnabledDrawable);

        mAmountPlusBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mAmountPlusBtn.setBackground(mPlusPressedDrawable);
                        mAmountPlusBtn.getDrawable().setTint(iconTextColor);

                        //round & minus value
                        addToEnterAmount(AMOUNT_UNIT);

                        return true;
                    case MotionEvent.ACTION_UP:
                        mAmountPlusBtn.setBackground(mPlusEnabledDrawable);
                        mAmountPlusBtn.getDrawable().setTint(dividerColor);
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
                        mAmountMinusBtn.getDrawable().setTint(iconTextColor);

                        //round & minus value
                        addToEnterAmount(-AMOUNT_UNIT);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mAmountMinusBtn.setBackground(mMinusEnabledDrawable);
                        mAmountMinusBtn.getDrawable().setTint(dividerColor);
                        return true;
                }
                return false;
            }
        });

        //toClear focus of editext
        mLoanDetailRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //map touch to edittext
                    if (mEnterAmount.isFocused()) {
                        Rect outRect = new Rect();
                        mEnterAmount.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            mEnterAmount.clearFocus();
                            InputMethodManager imm = (InputMethodManager)
                                    v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });

        //toClear focus when press keypad enter
        mEnterAmount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int key, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (key) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_NUMPAD_ENTER:
                            mEnterAmount.clearFocus();
                            InputMethodManager imm = (InputMethodManager)
                                    v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        default:
                            break;
                    }
                }
                return false;
            }
        });


    }

    public void attachView(final LoanDetail loanDetail, final Context context) {

        mLoanIdenTextView.setText(loanDetail.loanId);
        mPercentageReturn.setText(Double.toString(loanDetail.interestRate));
        mGrade.setText(loanDetail.grade);
        
        if(loanDetail.grade != null) {
            switch (loanDetail.grade) {
                case "A+":
                    mGrade.setTextColor(colorAPlus);
                    break;
                case "A":
                    mGrade.setTextColor(colorA);
                    break;
                case "B+":
                    mGrade.setTextColor(colorBPlus);
                    break;
                case "B":
                    mGrade.setTextColor(colorB);
                    break;
                case "C":
                    mGrade.setTextColor(colorC);
                    break;
                case "D":
                    mGrade.setTextColor(colorD);
                    break;
                case "E":
                    mGrade.setTextColor(colorE);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid grade " + loanDetail.grade);
            }
        }

        if(loanDetail.security != null) {
            switch (loanDetail.security) {
                case IN_SEC_COLLATERAL:
                    mSecurityIcon.setImageDrawable(mShieldOutlineIcon);
                    mSecurityIcon.setColorFilter(shieldColor);
                    mSecurityDescription.setText(WordUtils.wrap(
                            WordUtils.capitalize(loanDetail.collateral.replaceAll("_", " ")
                                    + "\n" + IN_SEC_COLLATERAL), 25));
                    break;
                case IN_SEC_UNCOLLATERALIZED:
                    mSecurityIcon.setImageDrawable(mLockOpenIcon);
                    mSecurityIcon.setColorFilter(lockOpenColor);
                    mSecurityDescription.setText(OUT_SEC_UNCOLLATERALIZED);
                    break;
                case IN_SEC_INVOICE_OR_CHEQUE:
                    mSecurityIcon.setImageDrawable(mFileIcon);
                    mSecurityIcon.setColorFilter(fileColor);
                    mSecurityDescription.setText(OUT_SEC_INVOICE_OR_CHEQUE);
                    break;
            }
        }

        int progressNum = loanDetail.fundedPercentageCache;
        mProgressBar.setProgress(progressNum);
        mProgressDescription.setText(progressNum + "%\nFunded");

        mTenureDuration.setText(Integer.toString(loanDetail.tenure));

        if(loanDetail.frequency != null) {
            String termDescription = OUT_FREQUENCY_MONTH_LABEL;
            if (!loanDetail.frequency.equals(IN_FREQUENCY_MONTH_VALUE))
                termDescription = loanDetail.frequency;
            mTenureDescription.setText(termDescription);
        }

        if(loanDetail.fundingEndDate != null) {
            int daysLeft = DateUtils.findDaysLeft(DATE_TIME_REGION, loanDetail.fundingEndDate);

            if (daysLeft > 0) {
                mNumDaysLeft.setText(Integer.toString(daysLeft));
            } else {
                mNumDaysLeft.setText("0");
            }
        }


        if(loanDetail.currency != null) {
            mTargetAmount.setText(NumericUtils.truncateNumber(loanDetail.targetAmount));
            mTargetAmountDescription.setText(mTargetAmountPrincipalString + " (" + loanDetail.currency + ")");
        }

        if(loanDetail.frequency != null) {
            String scheduleTermDescription = OUT_FREQUENCY_MONTH_SCHEDULE_LABEL;
            if (!loanDetail.frequency.equals(IN_FREQUENCY_MONTH_VALUE))
                scheduleTermDescription = loanDetail.frequency;
            mScheduleFrequencyLabel.setText(scheduleTermDescription);
        }

        if(loanDetail.startDate != null)
            mScheduleStartDate.setText(DateUtils.dateTimeFormatter(OUT_DATE_TIME_FORMAT, loanDetail.startDate));
        if(loanDetail.firstRepayment != null)
            mScheduleFirstRepaymentDate.setText(DateUtils.dateTimeFormatter(OUT_DATE_TIME_FORMAT, loanDetail.firstRepayment));
        if(loanDetail.lastRepayment != null)
            mScheduleLastRepaymentDate.setText(DateUtils.dateTimeFormatter(OUT_DATE_TIME_FORMAT, loanDetail.lastRepayment));

        if(loanDetail.currency != null)
            mAvalibleAmount.setText(NumericUtils.formatCurrency(loanDetail.currency,
                    loanDetail.fundingAvalibleAmount, loanDetail.currency+" ", false) + " " + loanDetail.currency);

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
