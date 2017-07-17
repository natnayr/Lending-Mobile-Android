package com.crowdo.p2pconnect.view.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crowdo.p2pconnect.commons.MemberDataRetrieval;
import com.crowdo.p2pconnect.custom_ui.CartBadgeDrawable;
import com.crowdo.p2pconnect.data.client.BiddingClient;
import com.crowdo.p2pconnect.data.client.LoanClient;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.response.BidOnlyResponse;
import com.crowdo.p2pconnect.model.response.CheckBidResponse;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
import com.crowdo.p2pconnect.model.response.MessageResponse;
import com.crowdo.p2pconnect.model.others.Server;
import com.crowdo.p2pconnect.oauth.AuthAccountUtils;
import com.crowdo.p2pconnect.helpers.PermissionsUtils;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.model.response.LoanDetailResponse;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.view.activities.CheckoutActivity;
import com.crowdo.p2pconnect.view.activities.Henson;
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
import butterknife.BindView;
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

    @BindString(R.string.downloading_label) String mToastDownloadingMessage;
    @BindString(R.string.downloaded_to_label) String mDownloadedToLabel;
    @BindString(R.string.loan_detail_error_reading_pdf) String mSnackPDFReadErrorLabel;
    @BindString(R.string.intent_file_chooser) String mIntentChooserLabel;
    @BindString(R.string.unable_open_file_label) String mErrorOpenFileLabel;
    @BindString(R.string.loan_detail_prog_snackbar_bid_too_low) String mBidTooLowLabel;
    @BindString(R.string.permissions_write_request) String mPermissionRequestLabel;
    @BindString(R.string.cancel_label) String mCancelLabel;
    @BindString(R.string.okay_label) String mOkayLabel;
    @BindString(R.string.open_label) String mOpenLabel;
    @BindString(R.string.permissions_no_write_statement) String mCannotWriteLabel;
    @BindString(R.string.loan_detail_invalid_investor_label) String mInvalidInvestorLabel;
    @BindString(R.string.loan_detail_invalid_investor_button_label) String mInvalidInvestorButtonLabel;
    @BindView(R.id.loan_details_swiperefresh) SwipeRefreshLayout mSwipeContainer;


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
    private MenuItem mMenuCart;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_loan_details, container, false);
        ButterKnife.bind(this, rootView);

        viewHolder = new LoanDetailsViewHolder(rootView, getActivity());
        viewHolder.initView();

        //set file download button here..
        viewHolder.mFactsheetDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PermissionsUtils.checkPermisssionAndRequestStorageFragment(getActivity(),
                        LoanDetailsFragment.this, mPermissionRequestLabel, mOkayLabel,
                        mCancelLabel);

                downloadFactSheet();
            }
        });

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateLoanDetails();
            }
        });

        mSwipeContainer.setColorSchemeResources(R.color.color_primary_700,
                R.color.color_primary, R.color.color_primary_300);

        rootView.setTag(viewHolder);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SoftInputHelper.hideSoftKeyboard(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewHolder != null) {
            //needs to refresh and check user state
            viewHolder.mBidEnterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkingBid();
                }
            });
        }

        populateLoanDetails();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        mMenuCart = menu.findItem(R.id.action_cart);
        updateShoppingCartItemCount();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        Log.d(LOG_TAG, "APP onCreateOptionsMenu inflate action_cart");
        inflater.inflate(R.menu.menu_cart, menu);
        mMenuCart = menu.findItem(R.id.action_cart);
        updateShoppingCartItemCount();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_cart:
                Log.d(LOG_TAG, "APP onOptionsItemSelected action_cart called");
                Intent checkoutIntent = new Intent(getActivity(), CheckoutActivity.class);
                checkoutIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(checkoutIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
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
        if(disposablePostAcceptBid != null){
            disposablePostAcceptBid.dispose();
        }


        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void populateLoanDetails(){
        getActivity().invalidateOptionsMenu();
        updateShoppingCartItemCount();

        Log.d(LOG_TAG, "APP populateLoanDetails()");
        LoanClient.getInstance(getActivity())
                .getLoanDetails(LoanDetailsFragment.this.initLoanId,
                        ConstantVariables.getUniqueAndroidID(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<LoanDetailResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableGetLoanDetails = d;
                    }

                    @Override
                    public void onNext(@NonNull Response<LoanDetailResponse> response) {
                        if(response.isSuccessful()){
                            LoanDetailResponse loanDetailResponse = response.body();

                            if(loanDetailResponse != null) {
                                mLoanDetailResponse = loanDetailResponse;
                                Log.d(LOG_TAG, "APP populateLoanDetails Rx onNext with loanId "
                                        + loanDetailResponse.getLoan().getLoanId() + " retreived.");
                                viewHolder.attachView(loanDetailResponse, getActivity());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                        mSwipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP getLoanDetails Rx onComplete");
                        mSwipeContainer.setRefreshing(false);
                    }
                });
    }

    private void downloadFactSheet(){

        if(PermissionsUtils.checkPermissionOnly(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && PermissionsUtils.checkPermissionOnly(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            if (initLoanId >= 0) {
                Toast.makeText(getActivity(), mToastDownloadingMessage, Toast.LENGTH_SHORT).show();

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
                                        getView(), mDownloadedToLabel + s, Snackbar.LENGTH_LONG);

                                snackbar.setAction(mOpenLabel, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            File downloadedFactsheet = new File(new URI(s));

                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setDataAndType(Uri.fromFile(downloadedFactsheet),
                                                    ConstantVariables.PDF_CONTENT_TYPE);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            Intent chooserIntent = Intent.createChooser(intent,
                                                    mIntentChooserLabel);

                                            startActivity(chooserIntent);
                                        } catch (ActivityNotFoundException e) {
                                            Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);

                                            final Snackbar snackbar = SnackBarUtil
                                                    .snackBarForWarningCreate(getView(),
                                                            mSnackPDFReadErrorLabel,
                                                            Snackbar.LENGTH_LONG);
                                            snackbar.setAction(mOkayLabel, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    snackbar.dismiss();
                                                }
                                            });
                                            snackbar.show();
                                        } catch (URISyntaxException ue){
                                            Log.e(LOG_TAG, "ERROR: " + ue.getMessage(), ue);
                                            final Snackbar snackbar = SnackBarUtil
                                                    .snackBarForWarningCreate(getView(),
                                                            mErrorOpenFileLabel,
                                                            Snackbar.LENGTH_LONG);

                                            snackbar.setAction(mOkayLabel, new View.OnClickListener() {
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
                final Snackbar snackbar = SnackBarUtil.snackBarForWarningCreate(
                        getView(), mBidTooLowLabel, Snackbar.LENGTH_LONG);

                snackbar.setAction(mOkayLabel, new View.OnClickListener() {
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
                        public void onSubscribe(@NonNull Disposable d) {
                            disposablePostCheckBid = d;
                        }

                        @Override
                        public void onNext(@NonNull Response<CheckBidResponse> response) {
                            Log.d(LOG_TAG, "APP checkingBid onNext");
                            if(response.isSuccessful()){
                                String serverMessage;
                                CheckBidResponse checkBidResponse = response.body();
                                if(checkBidResponse != null){
                                    Log.d(LOG_TAG, "APP " + checkBidResponse.getServer().getMessage());
                                    final long originalAmount = checkBidResponse.getOriginalInvestAmount();
                                    final long newAmount = checkBidResponse.getInvestAmount();
                                    if(originalAmount != newAmount){
                                        //amount is adjusted
                                        long newUnitAmount = newAmount / ConstantVariables.IDR_BASE_UNIT;
                                        viewHolder.mEnterAmount.setText(Long.toString(newUnitAmount));

                                        serverMessage = checkBidResponse.getServer().getMessage();
                                        Snackbar changeBidSnackBar = SnackBarUtil.snackBarForWarningCreate(
                                                getView(), serverMessage, Snackbar.LENGTH_SHORT);
                                        changeBidSnackBar.addCallback(new Snackbar.Callback(){
                                            @Override
                                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                                addToCart(newAmount);
                                            }
                                        });
                                        changeBidSnackBar.show();
                                    }else{
                                        addToCart(newAmount);
                                    }
                                }
                            }else{
                                //Error Handling
                                if(HTTPResponseUtils.check4xxClientError(response.code())){
                                    if (ConstantVariables.HTTP_INVESTOR_FAILED_ACCREDITATION == response.code()){
                                        Snackbar investorInvalidSnackbar = SnackBarUtil.snackBarForWarningCreate(getView(),
                                                mInvalidInvestorLabel, Snackbar.LENGTH_LONG);

                                        investorInvalidSnackbar.setAction(mInvalidInvestorButtonLabel, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String webViewUrl = APIServices.P2P_BASE_URL +
                                                        "mobile2/register_as_investor" +
                                                        "?lang=" + LocaleHelper.getLanguage(getActivity()) +
                                                        "&device_id=" +
                                                        ConstantVariables.getUniqueAndroidID(getActivity());

                                                Log.d(LOG_TAG, "APP Top Up Action webViewUrl " + webViewUrl);

                                                Intent intent = Henson.with(getActivity())
                                                        .gotoWebViewActivity()
                                                        .mUrl(webViewUrl)
                                                        .build();
                                                startActivity(intent);
                                            }
                                        });

                                        investorInvalidSnackbar.show();
                                    }else{
                                        //Invalid Investment Amount (e.g. 0, -1, etc)
                                        String serverErrorMessage = "Error: Check Bid Not successful";
                                        if(response.errorBody() != null) {
                                            Converter<ResponseBody, MessageResponse> errorConverter =
                                                    bidClient.getRetrofit().responseBodyConverter(
                                                            MessageResponse.class, new Annotation[0]);
                                            try{
                                                MessageResponse errorResponse = errorConverter
                                                        .convert(response.errorBody());

                                                serverErrorMessage = errorResponse
                                                        .getServer().getMessage();
                                            }catch (IOException e){
                                                e.printStackTrace();
                                                Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                                            }

                                            SnackBarUtil.snackBarForWarningCreate(getView(),
                                                    serverErrorMessage, Snackbar.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
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
                .subscribe(new Observer<Response<BidOnlyResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposablePostAcceptBid = d;
                    }

                    @Override
                    public void onNext(Response<BidOnlyResponse> response) {
                        if(response.isSuccessful()){
                            BidOnlyResponse acceptBidOnlyResponse = response.body();
                            if(acceptBidOnlyResponse != null){
                                Investment bid = acceptBidOnlyResponse.getBid();
                                Server server = acceptBidOnlyResponse.getServer();
                                Log.d(LOG_TAG, "APP addToCart onNext() bid_id: " + bid.getId());

                                //pass response to user
                                SnackBarUtil.snackBarForSuccessCreate(getView(),
                                        server.getMessage(), Snackbar.LENGTH_SHORT).show();

                                updateShoppingCartItemCount(); //update cart to new number
                                viewHolder.mEnterAmount.setText(""); //clear bidding after
                            }
                        }else{
                            Log.d(LOG_TAG, "APP getCheckoutSummary !isSuccessful onNext() status > "
                                    + response.code());
                            //Error Handling
                            if(HTTPResponseUtils.check4xxClientError(response.code())){

                                if(ConstantVariables.HTTP_PRECONDITION_FAILED == response.code()){
                                    String serverErrorMessage = "Error: Accept Bid Not Successful";
                                    //Invalid Investment Amount (e.g. 0, -1, etc)

                                    SnackBarUtil.snackBarForWarningCreate(getView(),
                                            serverErrorMessage, Snackbar.LENGTH_SHORT)
                                            .show();

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

    private void updateShoppingCartItemCount(){

        if(mMenuCart == null) {
            Log.d(LOG_TAG, "APP updateShoppingCartItemCount mMenuCart is null");
            getActivity().invalidateOptionsMenu();
            return;
        }

        final LayerDrawable cartIcon = (LayerDrawable) mMenuCart.getIcon();
        MemberDataRetrieval memberRetrieval = new MemberDataRetrieval();
        memberRetrieval.retrieveMemberInfo(getActivity(), new CallBackUtil<MemberInfoResponse>() {
            @Override
            public void eventCallBack(MemberInfoResponse memberInfoResponse) {
                if (HTTPResponseUtils.check2xxSuccess(memberInfoResponse
                        .getServer().getStatus())) {
                    CartBadgeDrawable.setBadgeCount(getActivity(), cartIcon,
                            Integer.toString(memberInfoResponse.getNumberOfPendingBids()));
                }
            }
        });
    }

}
