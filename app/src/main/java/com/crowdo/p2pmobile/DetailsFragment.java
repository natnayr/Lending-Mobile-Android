package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crowdo.p2pmobile.custom_ui.GoalProgressBar;
import com.crowdo.p2pmobile.data.LoanDetail;
import com.crowdo.p2pmobile.data.LoanDetailClient;
import com.crowdo.p2pmobile.helper.FontManager;

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
                    Log.d(LOG_TAG, "ERROR: " + e.getMessage());
                }

                @Override
                public void onNext(LoanDetail loanDetail) {
                    Log.d(LOG_TAG, "TEST: populated LOANDETAILS Rx onNext with :"
                            + loanDetail.id + " retreived.");
                    viewHolder.attachView(loanDetail, getActivity());
                    Log.d(LOG_TAG, "TEST: View exists = " + (getView() != null));
                }
            });
    }

    static class LoanDetailsViewHolder {

        // static views
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
//        @BindView(R.id.loan_detail_factsheet_download_btn) Button mFactsheetDownloadBtn;


        // color
        @BindColor(R.color.color_icons_text) int iconTextColor; //white
        @BindColor(R.color.color_divider) int dividerColor; //white

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
        }

        public void attachView(LoanDetail loanDetail, Context context){
            mProgressBar.setProgress(75);

            mSecurityIcon.setText(R.string.fa_shield);
        }

    }
}
