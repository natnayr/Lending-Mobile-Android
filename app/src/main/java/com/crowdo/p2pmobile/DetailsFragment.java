package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crowdo.p2pmobile.custom_ui.GoalProgressBar;
import com.crowdo.p2pmobile.data.LoanDetail;
import com.crowdo.p2pmobile.data.LoanDetailClient;
import com.crowdo.p2pmobile.data.LoanListClient;
import com.crowdo.p2pmobile.data.LoanListItem;
import com.crowdo.p2pmobile.helper.FontManager;
import com.f2prateek.dart.InjectExtra;

import java.util.List;

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
    private String loanIDpassed = null;

    public DetailsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments()
                .getString(DetailsActivity.BUNDLE_LOANID_KEY) != null) {
            String loanIDParsed = getArguments().getString(DetailsActivity.BUNDLE_LOANID_KEY);
            Log.d(LOG_TAG, "TEST: passed loanIDParsed: " + loanIDParsed);
            this.loanIDpassed = loanIDParsed;
            // get data
            populateLoanDetails(loanIDParsed);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, parent, false);

        final LoanDetailsViewHolder loanDetailsViewHolder = new LoanDetailsViewHolder(rootView);
        //init view first, attach after rxjava is done
        loanDetailsViewHolder.initView(getActivity());

        rootView.setTag(loanDetailsViewHolder);
        return rootView;
    }


    private void populateLoanDetails(final String loanIdOut){
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
                }
            });
    }

    static class LoanDetailsViewHolder {

        @BindView(R.id.loan_detail_progress_bar) GoalProgressBar mProgressBar;
        @BindView(R.id.loan_detail_collateral_icon_container) TextView mSecurityIcon;

        @BindView(R.id.loan_detail_enter_amount_edittext) EditText mEnterAmount;
        @BindView(R.id.loan_detail_amount_plus_btn) ImageButton mAmountPlusBtn;
        @BindView(R.id.loan_detail_amount_minus_btn) ImageButton mAmountMinusBtn;

        @BindColor(R.color.color_icons_text) int iconTextColor; //white
        @BindColor(R.color.color_divider) int dividerColor; //white

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
//            mProgressBar.setProgress(75);

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

//            mSecurityIcon.setText(R.string.fa_shield);

            Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
            FontManager.markAsIconContainer(mSecurityIcon, iconFont);
        }

        public void attachView(LoanDetail loanDetail, Context context){

        }

    }
}
