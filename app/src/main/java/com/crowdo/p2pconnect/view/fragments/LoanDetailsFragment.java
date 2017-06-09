package com.crowdo.p2pconnect.view.fragments;

import android.Manifest;
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

import com.crowdo.p2pconnect.data.client.BiddingClient;
import com.crowdo.p2pconnect.data.client.LoanClient;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.response.AcceptBidResponse;
import com.crowdo.p2pconnect.model.response.CheckBidResponse;
import com.crowdo.p2pconnect.model.response.MessageResponse;
import com.crowdo.p2pconnect.model.response.ServerResponse;
import com.crowdo.p2pconnect.oauth.AuthAccountUtils;
import com.crowdo.p2pconnect.helpers.PermissionsUtils;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.model.response.LoanDetailResponse;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.viewholders.LoanDetailsViewHolder;
import com.esafirm.rxdownloader.RxDownloader;
import com.f2prateek.dart.Dart;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import rx.Subscriber;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoanDetailsFragment extends Fragment {

    @BindColor(R.color.color_icons_text) int mColorIconText;

    @BindString(R.string.downloading_label) String mLabelToastDownloading;
    @BindString(R.string.downloaded_to_label) String mLabelDownloadedTo;
    @BindString(R.string.loan_detail_error_reading_pdf) String mLabelSnackPDFReadError;
    @BindString(R.string.intent_file_chooser) String mLabelIntentChooser;
    @BindString(R.string.unable_open_file_label) String mLabelErrorOpenFile;
    @BindString(R.string.loan_detail_prog_snackbar_bid_too_low) String mLabelBidTooLow;
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
    private Disposable disposablePostCheckBid;
    private Disposable disposablePostAcceptBid;
    private BiddingClient bidClient;

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

        viewHolder = new LoanDetailsViewHolder(rootView, getActivity());
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
//                    addToCart();
                    checkingBid();
                }
            });
        }

        populateLoanDetails();
    }

    @Override
    public void onPause() {
        if (alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
        if(disposableGetLoanDetails != null){
            disposableGetLoanDetails.dispose();
        }
        if(disposablePostCheckBid != null){
            disposablePostCheckBid.dispose();
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void populateLoanDetails(){

        LoanClient.getInstance(getActivity())
                .getLoanDetails(LoanDetailsFragment.this.initLoanId,
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
                                        + loanDetailResponse.getLoan().getLoanId() + " retreived.");
                                viewHolder.attachView(loanDetailResponse, getActivity());
                            }
                        }else{
                            if(HTTPResponseUtils.check4xxClientError(response.code())){
                                if(ConstantVariables.HTTP_UNAUTHORISED == response.code()){
                                    AuthAccountUtils.actionLogout(getActivity());
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
                        Log.d(LOG_TAG, "APP getLoanDetails Rx onComplete");
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

                                final Snackbar snackbar = SnackBarUtil.snackBarForInfoCreate(
                                        getView(), mLabelDownloadedTo + s, Snackbar.LENGTH_LONG);

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

                                            final Snackbar snackbar = SnackBarUtil
                                                    .snackBarForErrorCreate(getView(),
                                                            mLabelSnackPDFReadError,
                                                            Snackbar.LENGTH_LONG);
                                            snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    snackbar.dismiss();
                                                }
                                            });
                                            snackbar.show();
                                        } catch (URISyntaxException ue){
                                            Log.e(LOG_TAG, "ERROR: " + ue.getMessage(), ue);
                                            final Snackbar snackbar = SnackBarUtil
                                                    .snackBarForErrorCreate(getView(),
                                                            mLabelErrorOpenFile,
                                                            Snackbar.LENGTH_LONG);

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

    private void checkingBid(){
        if(viewHolder != null){
            int unitBidAmount;

            try {
                String inputUnitAmount = viewHolder.mEnterAmount.getText().toString().trim().replaceAll("[^\\d.]", "");
                unitBidAmount = (inputUnitAmount.equals("")) ? 0 : Integer.parseInt(inputUnitAmount);
            }catch (NumberFormatException nfe){
                Log.d(LOG_TAG, nfe.getMessage(), nfe);
                unitBidAmount = 0;
            }


            if(unitBidAmount <= 0 ) {
                final Snackbar snackbar = SnackBarUtil.snackBarForWarrningCreate(getView(),
                        mLabelBidTooLow, Snackbar.LENGTH_LONG);

                snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                }).show();
                return;
            }

            final long biddingAmount = unitBidAmount * ConstantVariables.IDR_BASE_UNIT;

            Log.d(LOG_TAG, "APP checkingBid of biddingAmount:"+biddingAmount
                    +" & initLoanId:"+initLoanId);

            //check bid
            bidClient = BiddingClient.getInstance(getActivity());

            bidClient.postCheckBid(biddingAmount, initLoanId,
                            ConstantVariables.getUniqueAndroidID(getActivity()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<CheckBidResponse>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposablePostCheckBid = d;
                        }

                        @Override
                        public void onNext(Response<CheckBidResponse> response) {
                            if(response.isSuccessful()){
                                String serverMessage;
                                CheckBidResponse checkBidResponse = response.body();
                                if(checkBidResponse != null){
                                    final long originalAmount = checkBidResponse.getOriginalInvestAmount();
                                    final long newAmount = checkBidResponse.getInvestAmount();
                                    if(originalAmount != newAmount){
                                        //amount is adjusted
                                        long newUnitAmount = newAmount / ConstantVariables.IDR_BASE_UNIT;
                                        viewHolder.mEnterAmount.setText(Long.toString(newUnitAmount));

                                        serverMessage = checkBidResponse.getServer().getMessage();
                                        SnackBarUtil.snackBarForWarrningCreate(getView(),
                                                serverMessage, Snackbar.LENGTH_SHORT).show();
                                    }

                                    addToCart(newAmount);
                                }
                            }else{
                                //Error Handling
                                if(HTTPResponseUtils.check4xxClientError(response.code())){
                                    String serverErrorMessage = "Error: Check Bid Not successful";
                                    if(ConstantVariables.HTTP_UNAUTHORISED == response.code()){
                                        AuthAccountUtils.actionLogout(getActivity());
                                    }else if(ConstantVariables.HTTP_PRECONDITION_FAILED == response.code()){
                                        //Invalid Investment Amount (e.g. 0, -1, etc)
                                        if(response.errorBody() != null) {
                                            Converter<ResponseBody, MessageResponse> errorConverter =
                                                    bidClient.getRetrofit().responseBodyConverter(
                                                            MessageResponse.class, new Annotation[0]);
                                            try{
                                                MessageResponse errorResponse = errorConverter
                                                        .convert(response.errorBody());

                                                serverErrorMessage = errorResponse
                                                        .getServerResponse().getMessage();
                                            }catch (IOException e){
                                                e.printStackTrace();
                                                Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                                            }

                                            SnackBarUtil.snackBarForErrorCreate(getView(),
                                                    serverErrorMessage, Snackbar.LENGTH_SHORT)
                                                    .show();
                                        }
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
                            Log.d(LOG_TAG, "APP postCheckBid Rx onComplete");
                        }
                    });
        }
    }

    private void addToCart(long biddingAmount){

        Log.d(LOG_TAG, "APP addToCart of biddingAmount:"+biddingAmount
                +" & initLoanId:"+initLoanId);
        bidClient.postAcceptBid(biddingAmount, initLoanId,
                ConstantVariables.getUniqueAndroidID(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<AcceptBidResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposablePostAcceptBid = d;
                    }

                    @Override
                    public void onNext(Response<AcceptBidResponse> response) {
                        if(response.isSuccessful()){
                            AcceptBidResponse acceptBidResponse = response.body();
                            if(acceptBidResponse != null){
                                Investment bid = acceptBidResponse.getExistingBid();
                                ServerResponse server = acceptBidResponse.getServer();

                                //pass response to user
                                SnackBarUtil.snackBarForInfoCreate(getView(),
                                        server.getMessage(), Snackbar.LENGTH_SHORT).show();

                            }
                        }else{
                            //Error Handling
                            if(HTTPResponseUtils.check4xxClientError(response.code())){
                                String serverErrorMessage = "Error: Accept Bid Not successful";
                                if(ConstantVariables.HTTP_UNAUTHORISED == response.code()){
                                    AuthAccountUtils.actionLogout(getActivity());
                                }else if(ConstantVariables.HTTP_PRECONDITION_FAILED == response.code()){
                                    //Invalid Investment Amount (e.g. 0, -1, etc)
                                    if(response.errorBody() != null) {
                                        Converter<ResponseBody, MessageResponse> errorConverter =
                                                bidClient.getRetrofit().responseBodyConverter(
                                                        MessageResponse.class, new Annotation[0]);
                                        try{
                                            MessageResponse errorResponse = errorConverter
                                                    .convert(response.errorBody());
                                            serverErrorMessage = errorResponse.getServerResponse().getMessage();
                                        }catch (IOException e){
                                            e.printStackTrace();
                                            Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                                        }

                                        SnackBarUtil.snackBarForErrorCreate(getView(),
                                                serverErrorMessage, Snackbar.LENGTH_SHORT)
                                                .show();
                                    }
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
                        Log.d(LOG_TAG, "APP checkBidAndAddToCart Rx onComplete");
                    }
                });
    }

}
