package com.crowdo.p2pconnect.view.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.PermissionsUtils;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.view.activities.Henson;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.model.LoanDetail;
import com.crowdo.p2pconnect.data.LoanDetailClient;
import com.crowdo.p2pconnect.data.LoanFactSheetClient;
import com.crowdo.p2pconnect.model.RegisteredMemberCheck;
import com.crowdo.p2pconnect.data.RegisteredMemberCheckClient;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.PerformEmailIdentityCheckTemp;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.viewholders.LoanDetailsViewHolder;
import com.f2prateek.dart.Dart;

import java.io.File;

import butterknife.BindColor;
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
public class LoanDetailsFragment extends Fragment {

    @BindColor(R.color.color_snackbar_teal_A200) int mColorSnackbarTealText;

    @BindString(R.string.downloading_label) String mLabelToastDownloading;
    @BindString(R.string.loan_detail_prog_snackbar_error_reading_pdf) String mLabelSnackPDFReadError;
    @BindString(R.string.intent_file_chooser) String mLabelIntentChooser;
    @BindString(R.string.loan_detail_prog_snackbar_bid_too_low_label) String mLabelBidTooLow;
    @BindString(R.string.loan_detail_prog_snackbar_bid_too_high_label) String mLabelBidTooHigh;
    @BindString(R.string.loan_detail_prog_snackbar_approved_investor_only) String mLabelApprovedInvestorOnly;
    @BindString(R.string.permissions_write_request) String mLabelPermissionRequest;
    @BindString(R.string.cancel_label) String mLabelCancel;
    @BindString(R.string.okay_label) String mLabelOkay;
    @BindString(R.string.open_label) String mLabelOpen;
    @BindString(R.string.permissions_no_write_statement) String mLabelCannotWrite;

    private static final String LOG_TAG = LoanDetailsFragment.class.getSimpleName();
    Subscription detailsSubscription;
    Subscription factsheetSubscription;
    Subscription memberCheckSubscription;

    private LoanDetailsViewHolder viewHolder;
    private LoanDetail mLoanDetail;
    private AlertDialog alertDialog;
    public static final String BUNDLE_ID_KEY = "LOAN_DETAILS_FRAGMENTS_ID_KEY";
    private int initLoanId;

    public LoanDetailsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initLoanId = Dart.get(getArguments(), BUNDLE_ID_KEY);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(LOG_TAG, "APP: onRequestPermissionsResult post requestCode="+requestCode);
        if(requestCode == ConstantVariables.REQUEST_CODE_PERMISSIONS_WRITE_EXTERNAL_STORAGE){
            downloadFactSheet();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_loan_details, parent, false);
        ButterKnife.bind(this, rootView);

        viewHolder = new LoanDetailsViewHolder(rootView);

        //Init view first,
        viewHolder.initView();

        detailsSubscription = LoanDetailClient.getInstance()
                .getLoanDetails(this.initLoanId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoanDetail>() {
                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "APP: Populated LoanDetail Rx onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(LoanDetail loanDetail) {
                        if(loanDetail != null) {
                            mLoanDetail = loanDetail;
                            Log.d(LOG_TAG, "APP: Populated LoanDetails Rx onNext with loanId "
                                    + loanDetail.getLoanId() + " retreived.");
                            viewHolder.attachView(loanDetail, getActivity());
                        }
                    }
                });

        //set file download button here..
        viewHolder.mFactsheetDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PermissionsUtils.checkPermisssionAndRequestStorageFragment(getActivity(),
                        LoanDetailsFragment.this, mLabelPermissionRequest, mLabelOkay,
                        mLabelCancel);

                downloadFactSheet();
            }
        });

        rootView.setTag(viewHolder);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SoftInputHelper.hideSoftKeyboard(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewHolder != null) {

            //needs to refresh and check user state
            viewHolder.mBidEnterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int acctMemberId = SharedPreferencesUtils.getSharedPrefInt(getActivity(),
                            ConstantVariables.PREF_KEY_USER_ID, -1);

                    if(acctMemberId == -1) {
                        DialogHolder dh = new DialogHolder();
                        dh.dialogEmailPrompt(getActivity());
                    }else {
                        addToCart();
                    }
                }
            });
        }
    }

    @Override
    public void onStop() {
        if(alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();

        super.onStop();
    }

    @Override
    public void onDestroy() {
        if(detailsSubscription != null && !detailsSubscription.isUnsubscribed()){
            detailsSubscription.unsubscribe();
        }
        if(factsheetSubscription != null && !factsheetSubscription.isUnsubscribed()){
            factsheetSubscription.unsubscribe();
        }

        if(memberCheckSubscription != null && !memberCheckSubscription.isUnsubscribed()){
            memberCheckSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    private void downloadFactSheet(){

        if(PermissionsUtils.checkPermissionOnly(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && PermissionsUtils.checkPermissionOnly(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

            if (initLoanId >= 0) {
                Toast.makeText(getActivity(),
                        mLabelToastDownloading, Toast.LENGTH_SHORT).show();

                factsheetSubscription = LoanFactSheetClient.getInstance(getActivity(), initLoanId)
                        .getLoanFactSheet()
                        .subscribe(new Observer<File>() {
                            @Override
                            public void onCompleted() {
                                Log.d(LOG_TAG, "APP: mFactSheetDownloadBtn complete");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(LOG_TAG, "ERROR: onError " + e.getMessage(), e);
                            }

                            @Override
                            public void onNext(final File file) {
                                final Snackbar snackbar = SnackBarUtil.snackBarCreate(getView(),
                                        file.getName(),
                                        mColorSnackbarTealText, Snackbar.LENGTH_LONG);

                                snackbar.setAction(mLabelOpen, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.fromFile(file), ConstantVariables.PDF_CONTENT_TYPE);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                                                | Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Intent chooserIntent = Intent.createChooser(intent, mLabelIntentChooser);

                                        try {
                                            startActivity(chooserIntent);
                                        } catch (ActivityNotFoundException e) {
                                            Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);

                                            final Snackbar snackbar = SnackBarUtil.snackBarCreate(getView(),
                                                    mLabelSnackPDFReadError,
                                                    mColorSnackbarTealText, Snackbar.LENGTH_LONG);
                                            snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    snackbar.dismiss();
                                                }
                                            });
                                            snackbar.show();
                                        }
                                    }
                                });
                                snackbar.show();
                            }
                        });
            }
        }
    }


    // WebView Intent into p2p crowdo
    private void addToCart(){
        if(viewHolder != null){
            int unitBidAmount;

            String email = SharedPreferencesUtils
                    .getSharedPrefString(getActivity(),
                    ConstantVariables.PREF_KEY_USER_EMAIL, null);

            boolean approvedToInvest = SharedPreferencesUtils
                    .getSharedPrefBool(getActivity(),
                            ConstantVariables.PREF_KEY_USER_INVESTOR_APPROVAL_IDR, false);


            if(email == null)
                return;

            try {
                String inputUnitAmount = viewHolder.mEnterAmount.getText().toString().trim().replaceAll("[^\\d.]", "");
                unitBidAmount = (inputUnitAmount.equals("")) ? 0 : Integer.parseInt(inputUnitAmount);
            }catch (NumberFormatException nfe){
                Log.d(LOG_TAG, nfe.getMessage(), nfe);
                unitBidAmount = 0;
            }

            long biddingAmount = unitBidAmount * ConstantVariables.IDR_BASE_UNIT;

            if(approvedToInvest == false){
                final Snackbar snackbar = SnackBarUtil.snackBarCreate(getView(),
                        mLabelApprovedInvestorOnly,
                        mColorSnackbarTealText);
                snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                })
                .show();
                return;
            }


            if(unitBidAmount <= 0 ) {
                final Snackbar snackbar = SnackBarUtil.snackBarCreate(getView(),
                        mLabelBidTooLow,
                        mColorSnackbarTealText);

                snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                })
                .show();
                return;

            }else if(mLoanDetail.getFundingAmountToCompleteCache() < biddingAmount){

                final Snackbar snackbar = SnackBarUtil.snackBarCreate(getView(),
                        mLabelBidTooHigh,
                        mColorSnackbarTealText);
                snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                }).show();
                return;
            }
            String localeKey = LocaleHelper.getLanguage(getActivity());

            String webViewUrl = APIServices.API_BASE_URL+
                    "mobile/login_and_checkout_authenticate?" +
                    "email="+email+"&loan_id="+initLoanId +
                    "&invest_amount="+biddingAmount+
                    "&market=idr&lang="+localeKey;

            Log.d(LOG_TAG, "APP: URL " + webViewUrl);

            Intent intent = Henson.with(getActivity())
                    .gotoWebViewActivity()
                    .mUrl(webViewUrl)
                    .build();

            startActivity(intent);
        }

    }


    class DialogHolder{
        /*
            Temp Dialog to identify user
         */
        @BindView(android.R.id.message) TextView memberCheckEmailTextView;
        @BindView(android.R.id.edit) EditText memberCheckEmailEditText;

        @BindString(R.string.member_check_email_dialog_label) String mMemberCheckEmailDialogLabel;
        @BindString(R.string.pref_user_email_default_value) String mMemberCheckEmailDialogDefaultValue;
        @BindString(R.string.sign_up_label) String mSignUpLabel;
        @BindString(R.string.proceed_label) String mProceedLabel;
        @BindString(R.string.cancel_label) String mCancelLabel;

        public void dialogEmailPrompt(final Context context){

            Log.d(LOG_TAG, "APP: Email Dialog Triggered");
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.pref_dialog_email_edittext_fix, null);
            ButterKnife.bind(this, dialogView);

            // setting dialog layout
            memberCheckEmailTextView.setText(mMemberCheckEmailDialogLabel);

            AlertDialog.Builder alertDialogBuilderInput = new AlertDialog.Builder(context);
            alertDialogBuilderInput.setView(dialogView);

            alertDialogBuilderInput
                    .setCancelable(true)
                    .setNegativeButton(mSignUpLabel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(APIServices.API_BASE_URL+"mobile/sign_up"));

                            startActivity(browserIntent);
                        }
                    })
                    .setPositiveButton(mProceedLabel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {

                        final String enteredEmail = memberCheckEmailEditText.getText()
                                .toString().toLowerCase().trim();

                        Log.d(LOG_TAG, "APP: enteredEmail is " + enteredEmail);

                        final PerformEmailIdentityCheckTemp idenCheck =
                                new PerformEmailIdentityCheckTemp(context);

                        memberCheckSubscription = RegisteredMemberCheckClient.getInstance()
                            .postUserCheck(enteredEmail)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<RegisteredMemberCheck>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.d(LOG_TAG, "ERROR: onError");
                                    idenCheck.onFailure(LOG_TAG, enteredEmail, e);
                                }

                                @Override
                                public void onNext(RegisteredMemberCheck registeredMemberCheck) {
                                    Log.d(LOG_TAG, "APP: onNext return " + registeredMemberCheck.memberId);
                                    if(idenCheck.onResponseCode(LOG_TAG, enteredEmail, registeredMemberCheck)) {
                                        addToCart();
                                    }
                                }
                        });

                        dialogInterface.dismiss();

                        }
                    }).setNeutralButton(mCancelLabel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.cancel();
                        }
                    });

            alertDialog = alertDialogBuilderInput.create();
            alertDialog.show();
        }
    }



}
