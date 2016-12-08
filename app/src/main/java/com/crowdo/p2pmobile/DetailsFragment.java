package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crowdo.p2pmobile.custom_ui.GoalProgressBar;
import com.crowdo.p2pmobile.data.LoanDetail;
import com.crowdo.p2pmobile.data.LoanDetailClient;
import com.crowdo.p2pmobile.helper.AmountRounder;
import com.crowdo.p2pmobile.helper.CurrencyNumberFormatter;
import com.crowdo.p2pmobile.helper.FontManager;

import org.apache.commons.lang3.text.WordUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;


import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {

    private static final String IN_FREQUENCY_MONTH_VALUE = "Monthly";
    private static final String OUT_FREQUENCY_MONTH_VALUE = "Months Tenure";

    private static final String IN_SEC_COLLATERAL = "Collateral";
    private static final String OUT_SEC_COLLATERAL = "";
    private static final String IN_SEC_UNCOLLATERALIZED = "Uncollateralized";
    private static final String OUT_SEC_UNCOLLATERALIZED = "No Collateral";
    private static final String IN_SEC_INVOICE_OR_CHEQUE = "Working Order/Invoice";
    private static final String OUT_SEC_INVOICE_OR_CHEQUE = "Working Order/\nInvoice";


    private static final long AMOUNT_UNIT = 1000000;
    private static final int ENTER_AMOUNT_MAX_LENGTH = 18;
    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();
    private Subscription subscription;
    private String initLoanId = null;

    public DetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments()
                .getString(DetailsActivity.BUNDLE_LOANID_KEY) != null) {
            this.initLoanId = getArguments()
                    .getString(DetailsActivity.BUNDLE_LOANID_KEY); //store
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_details, parent, false);

        final LoanDetailsViewHolder viewHolder = new LoanDetailsViewHolder(rootView);
        //init view first, attach after rxjava is done
        viewHolder.initView(getActivity());

        subscription = LoanDetailClient.getInstance()
                .getLoanDetails(this.initLoanId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoanDetail>() {

                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "TEST: populated LOANDETAILS Rx onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage());
                    }

                    @Override
                    public void onNext(LoanDetail loanDetail) {
                        Log.d(LOG_TAG, "TEST: populated LOANDETAILS Rx onNext with :"
                                + loanDetail.id + " retreived.");
                        viewHolder.attachView(loanDetail, getActivity());
                    }
                });

        rootView.setTag(viewHolder);
        return rootView;
    }


//    private void populateLoanDetails(final String loanIdOut, final LoanDetailsViewHolder viewHolder,
//                                     final ProgressDialog progressDialog){
//
//    }

    static class LoanDetailsViewHolder {

        // static views
        @BindView(R.id.loan_details) RelativeLayout mLoanDetailRelativeLayout;
        @BindView(R.id.loan_detail_iden_no) TextView mLoanIdenTextView;
        @BindView(R.id.loan_detail_percentage_return) TextView mPercentageReturn;
        @BindView(R.id.loan_detail_grade) TextView mGrade;
        @BindView(R.id.loan_detail_security_icon_container) TextView mSecurityIcon;
        @BindView(R.id.loan_detail_security_description) TextView mSecurityDescription;
        @BindView(R.id.loan_detail_progress_bar) GoalProgressBar mProgressBar;
        @BindView(R.id.loan_detail_progress_description) TextView mProgressDescription;
        @BindView(R.id.loan_detail_target_amount) TextView mTargetAmount;
        @BindView(R.id.loan_detail_target_amount_currency) TextView mTargetAmountCurrency;
        @BindView(R.id.loan_detail_avalible_amount) TextView mAvalibleAmount;
        @BindView(R.id.loan_detail_avalible_amount_currency) TextView mAvalibleAmountCurrency;
        @BindView(R.id.loan_detail_tenure_duration) TextView mTenureDuration;
        @BindView(R.id.loan_detail_tenure_description) TextView mTenureDescription;
        @BindView(R.id.loan_detail_days_left) TextView mNumDaysLeft;

        // to interact with
        @BindView(R.id.loan_detail_amount_plus_btn) ImageButton mAmountPlusBtn;
        @BindView(R.id.loan_detail_enter_amount_edittext_layout) TextInputLayout mEnterAmountTextLayout;
        @BindView(R.id.loan_detail_enter_amount_edittext) EditText mEnterAmount;
        @BindView(R.id.loan_detail_amount_minus_btn) ImageButton mAmountMinusBtn;
        @BindView(R.id.loan_detail_factsheet_download_btn) LinearLayout mFactsheetDownloadBtn; //LinearLayout act as button
        @BindView(R.id.loan_detail_bid_enter_btn) LinearLayout mBidEnterBtn; //LinearLayout act as button

        // String Bindings
        @BindString(R.string.date_time_region) String DATE_TIME_REGION;
        @BindString(R.string.loan_detail_enter_amount_hint_error_positive) String errorPositiveMsg;
        @BindString(R.string.loan_detail_enter_amount_hint_error_please_clear) String errorClearMsg;
        @BindString(R.string.loan_detail_enter_amount_hint_default) String hintDefaultMsg;
        @BindString(R.string.loan_detail_enter_amount_hint_rounded) String hintRoundedMsg;

        // color
        @BindColor(R.color.fa_icon_shield) int shieldColor;
        @BindColor(R.color.fa_icon_file_text) int fileColor;
        @BindColor(R.color.fa_icon_unlock_alt) int unlockAltColor;
        @BindColor(R.color.color_icons_text) int iconTextColor; //white
        @BindColor(R.color.color_divider) int dividerColor;
        @BindColor(R.color.grade_color_A_plus) int colorAPlus;
        @BindColor(R.color.grade_color_A) int colorA;
        @BindColor(R.color.grade_color_B_plus) int colorBPlus;
        @BindColor(R.color.grade_color_E) int colorB;
        @BindColor(R.color.grade_colorC) int colorC;
        @BindColor(R.color.grade_color_D) int colorD;
        @BindColor(R.color.grade_color_E) int colorE;
        @BindColor(R.color.edittext_hint_color) int hintColor;
        @BindColor(R.color.edittext_error_color) int errorColor;

        //drawables extras
        @BindDrawable(R.drawable.loan_detail_plus_bid_btn_enabled) Drawable mPlusEnabledDrawable;
        @BindDrawable(R.drawable.loan_detail_plus_bid_btn_pressed) Drawable mPlusPressedDrawable;
        @BindDrawable(R.drawable.loan_detail_plus_bid_btn_focused) Drawable mPlusFocusedDrawable;
        @BindDrawable(R.drawable.loan_detail_plus_bid_btn_disabled) Drawable mPlusDisabledDrawable;
        @BindDrawable(R.drawable.loan_detail_minus_bid_btn_enabled) Drawable mMinusEnabledDrawable;
        @BindDrawable(R.drawable.loan_detail_minus_bid_btn_pressed) Drawable mMinusPressedDrawable;
        @BindDrawable(R.drawable.loan_detail_minus_bid_btn_focused) Drawable mMinusFocusedDrawable;
        @BindDrawable(R.drawable.loan_detail_minus_bid_btn_disabled) Drawable mMinusDisabledDrawable;

        public LoanDetailsViewHolder(View view){
            ButterKnife.bind(this, view);
        }


        public void initView(Context context){

            mAmountPlusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //add base unit to edittext
                }
            });

            mAmountMinusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //add base unit to edittext
                }
            });

            mAmountPlusBtn.setBackground(mPlusEnabledDrawable);
            mAmountMinusBtn.setBackground(mMinusEnabledDrawable);




            Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
            FontManager.markAsIconContainer(mSecurityIcon, iconFont);

            //toClear focus of editext
            mLoanDetailRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        //map touch to edittext
                        if(mEnterAmount.isFocused()){
                            Rect outRect = new Rect();
                            mEnterAmount.getGlobalVisibleRect(outRect);
                            if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
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
                    if(event.getAction() == KeyEvent.ACTION_DOWN){
                        switch (key){
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

            //default settings
            mEnterAmountTextLayout.setErrorEnabled(false);
            mEnterAmountTextLayout.setHintTextAppearance(R.style.HintAppearance);
        }

        public void attachView(final LoanDetail loanDetail, final Context context){


            mLoanIdenTextView.setText(loanDetail.loanIdOut);
            mPercentageReturn.setText(Double.toString(loanDetail.interestRateOut));
            mGrade.setText(loanDetail.grade);

            switch (loanDetail.grade) {
                case "A+": mGrade.setTextColor(colorAPlus);
                    break;
                case "A": mGrade.setTextColor(colorA);
                    break;
                case "B+": mGrade.setTextColor(colorBPlus);
                    break;
                case "B": mGrade.setTextColor(colorB);
                    break;
                case "C": mGrade.setTextColor(colorC);
                    break;
                case "D": mGrade.setTextColor(colorD);
                    break;
                case "E": mGrade.setTextColor(colorE);
                    break;
            }

            switch(loanDetail.security){
                case IN_SEC_COLLATERAL:
                    mSecurityIcon.setText(R.string.fa_shield);
                    mSecurityIcon.setTextColor(shieldColor);
                    mSecurityDescription.setText(WordUtils.wrap(
                            WordUtils.capitalize(loanDetail.collateralOut.replaceAll("_", " ")
                                    + "\n" + IN_SEC_COLLATERAL), 25));
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

            int progressNum = loanDetail.fundedPercentageCache;
            mProgressBar.setProgress(progressNum);
            mProgressDescription.setText(progressNum+"%\nFunded");

            mTargetAmount.setText(CurrencyNumberFormatter.formatCurrency(loanDetail.currencyOut,
                    loanDetail.targetAmountOut, loanDetail.currencyOut+" ", false));
            mTargetAmountCurrency.setText(loanDetail.currencyOut);

            mAvalibleAmount.setText(CurrencyNumberFormatter.formatCurrency(loanDetail.currencyOut,
                    loanDetail.fundingAmountToCompleteCache, loanDetail.currencyOut+" ", false));
            mAvalibleAmountCurrency.setText(loanDetail.currencyOut);

            mTenureDuration.setText(Integer.toString(loanDetail.tenureOut));

            String termDescription = OUT_FREQUENCY_MONTH_VALUE;
            if(!loanDetail.frequencyOut.equals(IN_FREQUENCY_MONTH_VALUE))
                termDescription = loanDetail.frequencyOut;
            mTenureDescription.setText(termDescription);

            DateTime sgNow = new DateTime(DateTimeZone.forID(DATE_TIME_REGION)); // set SG time
            DateTime endDate = new DateTime(loanDetail.fundingEndDate);
            int daysLeft = Days.daysBetween(sgNow.toLocalDate(), endDate.toLocalDate()).getDays();

            if(daysLeft<0){
                mNumDaysLeft.setText("0");

            }else{
                mNumDaysLeft.setText(daysLeft);
            }

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
                            String cleanString = s.toString().replaceAll("[^\\d]", "").trim();

                            //so as not to break Long maxNum, max ~trillion
                            if (cleanString.length() > 0 && cleanString.length() < ENTER_AMOUNT_MAX_LENGTH) {
                                long parsed = Long.parseLong(cleanString);

                                //validate negative
                                if(parsed < 0) {
                                    current = cleanString;
                                    mEnterAmountTextLayout.setHint(errorPositiveMsg);
                                    mEnterAmountTextLayout.setHintTextAppearance(R.style.ErrorAppearance);
                                }else{
                                    if (loanDetail.currencyOut.equals(CurrencyNumberFormatter.IDR)){
                                        current = CurrencyNumberFormatter.formatCurrency(
                                                loanDetail.currencyOut, parsed, "Rp ", true);
                                    }
                                }
                            } else {
                                //gave up
                                current = cleanString;
                            }

                            //fall through
                            mEnterAmount.setText(current);
                            mEnterAmount.setSelection(current.length());
                        }
                    }catch(NumberFormatException | IndexOutOfBoundsException e){
                        //catch long error
                        Log.e(LOG_TAG, "ERROR", e);
                        mEnterAmountTextLayout.setHint(errorClearMsg);
                        mEnterAmountTextLayout.setHintTextAppearance(R.style.ErrorAppearance);
                        //clear it for them
                        mEnterAmount.setText("");
                        mEnterAmount.setSelection(0);
                        current = "";
                    }finally {
                        mEnterAmount.addTextChangedListener(this);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            mEnterAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean onFocus) {
                    if(!onFocus){
                        mEnterAmount.getText().toString();
                    }
                }
            });


            mAmountPlusBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            mAmountPlusBtn.setBackground(mPlusPressedDrawable);
                            mAmountPlusBtn.getDrawable().setTint(iconTextColor);

                            //round & minus value
                            addToEnterAmount(AMOUNT_UNIT);
                            roundUpEnterAmount();

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
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            mAmountMinusBtn.setBackground(mMinusPressedDrawable);
                            mAmountMinusBtn.getDrawable().setTint(iconTextColor);

                            //round & minus value
                            addToEnterAmount(-AMOUNT_UNIT);
                            roundUpEnterAmount();
                            return true;
                        case MotionEvent.ACTION_UP:
                            mAmountMinusBtn.setBackground(mMinusEnabledDrawable);
                            mAmountMinusBtn.getDrawable().setTint(dividerColor);
                            return true;
                    }
                    return false;
                }
            });

        }

        private void addToEnterAmount(long byAmount) {
            String empty = "";
            String cleanString = mEnterAmount.getText().toString()
                    .replaceAll("[^\\d]", "").trim();

            if("0".equals(cleanString) || empty.equals(cleanString)){
                if(byAmount > 0) {
                    //post byAmount
                    mEnterAmount.setText(Long.toString(AMOUNT_UNIT));
                }
            }else if (cleanString.length() < ENTER_AMOUNT_MAX_LENGTH) {
                long curAmount = Long.parseLong(cleanString);
                if(curAmount >= 0 ) {
                    mEnterAmount.setText(Long.toString(curAmount + byAmount));
                }
            }
        }


        private void roundUpEnterAmount(){
            String empty = "";

            String valStr = mEnterAmount.getText().toString();
            if (!valStr.toString().equals(empty)) {
                String cleanString = valStr.toString().replaceAll("[^\\d]", "").trim();
                //within long value range
                if (cleanString.length() > 0 && cleanString.length() < ENTER_AMOUNT_MAX_LENGTH) {
                    long parsed = Long.parseLong(cleanString);

                    if ((parsed % AMOUNT_UNIT) == 0) {
                        //back to default
                        mEnterAmountTextLayout.setHint(hintDefaultMsg);
                        mEnterAmountTextLayout.setHintTextAppearance(R.style.HintAppearance);
                    } else {
                        mEnterAmount.setText(Long.toString(
                                AmountRounder.roundUpToNearestUnit(
                                        parsed, AMOUNT_UNIT)));
                        mEnterAmountTextLayout.setHint(hintRoundedMsg);
                        mEnterAmountTextLayout.setHintTextAppearance(R.style.HintAppearance);
                    }

                }
            }
        }

    }
}
