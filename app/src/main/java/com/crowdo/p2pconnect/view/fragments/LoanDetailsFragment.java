package com.crowdo.p2pconnect.view.fragments;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.oauth.AuthAccountUtils;
import com.crowdo.p2pconnect.helpers.PermissionsUtils;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.oauth.CrowdoAccountGeneral;
import com.crowdo.p2pconnect.view.activities.Henson;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.model.response.LoanDetailResponse;
import com.crowdo.p2pconnect.data.client.LoanDetailClient;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.viewholders.LoanDetailsViewHolder;
import com.esafirm.rxdownloader.RxDownloader;
import com.f2prateek.dart.Dart;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import rx.Subscriber;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoanDetailsFragment extends Fragment {

    @BindColor(R.color.color_icons_text) int mColorIconText;

    @BindString(R.string.downloading_label) String mLabelToastDownloading;
    @BindString(R.string.downloaded_to_label) String mLabelDownloadedTo;
    @BindString(R.string.loan_detail_prog_snackbar_error_reading_pdf) String mLabelSnackPDFReadError;
    @BindString(R.string.intent_file_chooser) String mLabelIntentChooser;
    @BindString(R.string.unable_open_file_label) String mLabelErrorOpenFile;
    @BindString(R.string.loan_detail_prog_snackbar_bid_too_low_label) String mLabelBidTooLow;
    @BindString(R.string.loan_detail_prog_snackbar_bid_too_high_label) String mLabelBidTooHigh;
    @BindString(R.string.permissions_write_request) String mLabelPermissionRequest;
    @BindString(R.string.cancel_label) String mLabelCancel;
    @BindString(R.string.okay_label) String mLabelOkay;
    @BindString(R.string.open_label) String mLabelOpen;
    @BindString(R.string.permissions_no_write_statement) String mLabelCannotWrite;

    private static final String LOG_TAG = LoanDetailsFragment.class.getSimpleName();

    private LoanDetailsViewHolder viewHolder;
    private LoanDetailResponse mLoanDetailResponse;
    private AlertDialog alertDialog;
    public static final String BUNDLE_ID_KEY = "LOAN_DETAILS_FRAGMENTS_ID_KEY";
    private int initLoanId;
    private Disposable disposableGetLoanDetails;

    public LoanDetailsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initLoanId = Dart.get(getArguments(), BUNDLE_ID_KEY);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(LOG_TAG, "APP onRequestPermissionsResult post requestCode="+requestCode);
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
        viewHolder.initView();

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
                    addToCart();
                }
            });
        }

        populateLoanDetails();
    }

    @Override
    public void onStop() {
        if(alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();

        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposableGetLoanDetails != null){
            if(!disposableGetLoanDetails.isDisposed()) {
                disposableGetLoanDetails.dispose();
            }
        }
    }

    private void populateLoanDetails(){
        String authToken = SharedPreferencesUtils.getSharedPrefString(getActivity(),
                CrowdoAccountGeneral.AUTHTOKEN_SHARED_PREF_KEY, null);
        if(authToken == null){
            AuthAccountUtils.actionLogout(AccountManager.get(getActivity()), getActivity());
        }

        LoanDetailClient.getInstance(getActivity())
                .getLoanDetails(authToken, LoanDetailsFragment.this.initLoanId,
                        ConstantVariables.getUniqueAndroidID(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<LoanDetailResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableGetLoanDetails = d;
                    }

                    @Override
                    public void onNext(Response<LoanDetailResponse> response) {
                        if(response.isSuccessful()){
                            LoanDetailResponse loanDetailResponse = response.body();

                            if(loanDetailResponse != null) {
                                mLoanDetailResponse = loanDetailResponse;
                                Log.d(LOG_TAG, "APP Populated LoanDetails Rx onNext with loanId "
                                        + loanDetailResponse.getLoanId() + " retreived.");
                                viewHolder.attachView(loanDetailResponse, getActivity());
                            }
                        }else{
                            if(HTTPResponseUtils.check4xxClientError(response.code())){
                                if(ConstantVariables.HTTP_UNAUTHORISED == response.code()){
                                    AuthAccountUtils.actionLogout(AccountManager.get(getActivity()),
                                            getActivity());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP Populated LoanDetail Rx onComplete");
                    }
                });
    }

    private void downloadFactSheet(){

        if(PermissionsUtils.checkPermissionOnly(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && PermissionsUtils.checkPermissionOnly(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            if (initLoanId >= 0) {
                Toast.makeText(getActivity(), mLabelToastDownloading, Toast.LENGTH_SHORT).show();

                final String url = APIServices.P2P_BASE_URL + APIServices.FACTSHEET_STAGE + initLoanId +
                        "/?" + APIServices.FACTSHEET_LANGUAGE_PARAM + LocaleHelper.getLanguage(getActivity());
                final String toFileName = "loan_" + initLoanId + "_factsheet.pdf";
                Log.d(LOG_TAG, "APP downloadFactSheet() called [" + url + "] for " + toFileName);

                RxDownloader.getInstance(getActivity())
                        .download(url, toFileName, ConstantVariables.PDF_CONTENT_TYPE)
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(LOG_TAG, "ERROR: onError " + e.getMessage(), e);
                            }

                            @Override
                            public void onNext(final String s) {

                                final Snackbar snackbar = SnackBarUtil.snackBarCreate(getView(),
                                mLabelDownloadedTo + s, mColorIconText,
                                Snackbar.LENGTH_LONG);

                                snackbar.setAction(mLabelOpen, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            File downloadedFactsheet = new File(new URI(s));

                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setDataAndType(Uri.fromFile(downloadedFactsheet),
                                                    ConstantVariables.PDF_CONTENT_TYPE);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            Intent chooserIntent = Intent.createChooser(intent,
                                                    mLabelIntentChooser);

                                            startActivity(chooserIntent);
                                        } catch (ActivityNotFoundException e) {
                                            Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);

                                            final Snackbar snackbar = SnackBarUtil.snackBarCreate(getView(),
                                                    mLabelSnackPDFReadError,
                                                    mColorIconText, Snackbar.LENGTH_LONG);
                                            snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    snackbar.dismiss();
                                                }
                                            });
                                            snackbar.show();
                                        } catch (URISyntaxException ue){
                                            Log.e(LOG_TAG, "ERROR: " + ue.getMessage(), ue);
                                            final Snackbar snackbar = SnackBarUtil.snackBarCreate(getView(),
                                                    mLabelErrorOpenFile,
                                                    mColorIconText);

                                            snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    snackbar.dismiss();
                                                }
                                            });
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

            try {
                String inputUnitAmount = viewHolder.mEnterAmount.getText().toString().trim().replaceAll("[^\\d.]", "");
                unitBidAmount = (inputUnitAmount.equals("")) ? 0 : Integer.parseInt(inputUnitAmount);
            }catch (NumberFormatException nfe){
                Log.d(LOG_TAG, nfe.getMessage(), nfe);
                unitBidAmount = 0;
            }

            long biddingAmount = unitBidAmount * ConstantVariables.IDR_BASE_UNIT;

            if(unitBidAmount <= 0 ) {
                final Snackbar snackbar = SnackBarUtil.snackBarCreate(getView(),
                        mLabelBidTooLow,
                        mColorIconText);

                snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                })
                .show();
                return;

            }else if(mLoanDetailResponse.getFundingAmountToCompleteCache() < biddingAmount){

                final Snackbar snackbar = SnackBarUtil.snackBarCreate(getView(),
                        mLabelBidTooHigh,
                        mColorIconText);
                snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                }).show();
                return;
            }
            String localeKey = LocaleHelper.getLanguage(getActivity());

            String webViewUrl = APIServices.P2P_BASE_URL +
                    "mobile2/checkout?" +
                    "loan_id="+initLoanId +
                    "&invest_amount="+biddingAmount+
                    "&lang="+localeKey+
                    "&device_id="+ConstantVariables.getUniqueAndroidID(getActivity());

            Log.d(LOG_TAG, "APP URL " + webViewUrl);

            Intent intent = Henson.with(getActivity())
                    .gotoWebViewActivity()
                    .mUrl(webViewUrl)
                    .build();

            startActivity(intent);
        }
    }

}
