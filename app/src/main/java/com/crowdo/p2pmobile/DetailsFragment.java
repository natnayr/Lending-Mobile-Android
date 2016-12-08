package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.crowdo.p2pmobile.helper.CurrencyNumberFormatter;
import com.crowdo.p2pmobile.helper.FontManager;

import org.apache.commons.lang3.text.WordUtils;


import butterknife.BindColor;
import butterknife.BindDrawable;
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
    private static final String OUT_FREQUENCY_MONTH_VALUE = "Months";

    private static final String IN_SEC_COLLATERAL = "Collateral";
    private static final String OUT_SEC_COLLATERAL = "";
    private static final String IN_SEC_UNCOLLATERALIZED = "Uncollateralized";
    private static final String OUT_SEC_UNCOLLATERALIZED = "No Collateral";
    private static final String IN_SEC_INVOICE_OR_CHEQUE = "Working Order/Invoice";
    private static final String OUT_SEC_INVOICE_OR_CHEQUE = "Working Order/\nInvoice";

    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();
    private Subscription subscription;
    private String loanId = null;

    public DetailsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments()
                .getString(DetailsActivity.BUNDLE_LOANID_KEY) != null) {
            this.loanId = getArguments()
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
        //get data and populate
        populateLoanDetails(this.loanId, viewHolder);

        rootView.setTag(viewHolder);
        return rootView;
    }


    private void populateLoanDetails(final String loanIdOut, final LoanDetailsViewHolder viewHolder){
        subscription = LoanDetailClient.getInstance()
            .getLoanDetails(loanIdOut)
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
    }

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
        @BindView(R.id.loan_detail_tenor_duration) TextView mTenorDuration;
        @BindView(R.id.loan_detail_tenor_description) TextView mTenorDescription;
        @BindView(R.id.loan_detail_days_left) TextView mNumDaysLeft;

        // to interact with
        @BindView(R.id.loan_detail_amount_plus_btn) ImageButton mAmountPlusBtn;
        @BindView(R.id.loan_detail_enter_amount_edittext) EditText mEnterAmount;
        @BindView(R.id.loan_detail_amount_minus_btn) ImageButton mAmountMinusBtn;
        @BindView(R.id.loan_detail_factsheet_download_btn) LinearLayout mFactsheetDownloadBtn; //LinearLayout act as button
        @BindView(R.id.loan_detail_bid_enter_btn) LinearLayout mBidEnterBtn; //LinearLayout act as button
//        @BindView()


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

            mAmountPlusBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            mAmountPlusBtn.setBackground(mPlusPressedDrawable);
                            mAmountPlusBtn.getDrawable().setTint(iconTextColor);
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
                            return true;
                        case MotionEvent.ACTION_UP:
                            mAmountMinusBtn.setBackground(mMinusEnabledDrawable);
                            mAmountMinusBtn.getDrawable().setTint(dividerColor);
                            return true;
                    }
                    return false;
                }
            });

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



        }

        public void attachView(final LoanDetail loanDetail, Context context){

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


            mEnterAmount.addTextChangedListener(new TextWatcher() {
                private String current = "";

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        if (!s.toString().equals(current)) {
                            mEnterAmount.removeTextChangedListener(this);

                            //remove all non-digit
                            String cleanString = s.toString().replaceAll("[^\\d]", "").trim();

                            //so as not to break Long maxNum, max ~trillion
                            if (cleanString.length() > 0 && cleanString.length() < 15) {
                                long parsed = Long.parseLong(cleanString);

                                current = CurrencyNumberFormatter.formatCurrency(loanDetail.currencyOut, parsed,
                                        "Rp ", true);
                            } else {
                                //gave up
                                current = cleanString;
                            }

                            mEnterAmount.setText(current);
                            mEnterAmount.setSelection(current.length());
                            mEnterAmount.addTextChangedListener(this);
                        }
                    }catch(NumberFormatException nfe){
                        //catch long error
                        Log.e(LOG_TAG, "Error: NumFormatException", nfe);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

    }
}
