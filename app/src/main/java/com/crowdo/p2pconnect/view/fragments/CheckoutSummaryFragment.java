package com.crowdo.p2pconnect.view.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.custom_ui.CheckoutSummaryItemTouchCallback;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.data.client.CheckoutClient;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.PermissionsUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.model.request.InvestBid;
import com.crowdo.p2pconnect.model.response.CheckoutSummaryResponse;
import com.crowdo.p2pconnect.model.response.CheckoutUpdateResponse;
import com.crowdo.p2pconnect.model.response.MessageResponse;
import com.crowdo.p2pconnect.oauth.AuthAccountUtils;
import com.crowdo.p2pconnect.view.activities.Henson;
import com.crowdo.p2pconnect.view.adapters.CheckoutSummaryAdapter;
import com.crowdo.p2pconnect.viewholders.CheckoutSummaryViewHolder;
import com.esafirm.rxdownloader.RxDownloader;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 22/5/17.
 */

public class CheckoutSummaryFragment extends Fragment{

    @BindView(R.id.checkout_summary_recycler_view) RecyclerView mCheckoutSummaryRecyclerView;

    @BindString(R.string.checkout_summary_empty_message_label) String mCheckoutSummaryEmptyMessage;
    @BindString(R.string.downloaded_to_label) String mLabelDownloadedTo;
    @BindString(R.string.okay_label) String mLabelOkay;
    @BindString(R.string.open_label) String mLabelOpen;
    @BindString(R.string.intent_file_chooser) String mLabelIntentFile;
    @BindString(R.string.unable_open_file_label) String mLabelErrorOpenFile;
    @BindString(R.string.downloading_label) String mLabelDownloading;

    @BindString(R.string.wait_message) String mLabelWaitMessage;

    private static final String LOG_TAG = CheckoutSummaryFragment.class.getSimpleName();
    private Context mContext;
    private CheckoutSummaryViewHolder mViewHolder;
    private CheckoutSummaryAdapter mCheckoutSummaryAdapter;
    private Disposable disposableGetCheckoutSummary;
    private Disposable disposablePostCheckoutUpdate;
    private Disposable disposablePostCheckoutConfirm;
    private ItemTouchHelperExtension mItemTouchHelper;
    private CheckoutClient checkoutClient;

    private static final int INTENT_REQUEST_TOP_UP = 10101;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_checkout_summary, container, false);
        ButterKnife.bind(this, rootView);

        this.mContext = getActivity();

        CallBackUtil<Boolean> callBackSummaryRefresh = new CallBackUtil<Boolean>() {
            @Override
            public void eventCallBack(Boolean doRefreshList) {
                populateSummaryList(doRefreshList);
            }
        };

        CallBackUtil<Boolean> callBackShowUpdateBtn = new CallBackUtil<Boolean>(){
            @Override
            public void eventCallBack(Boolean doShow) {
                if(doShow) {
                    mViewHolder.mSummaryUpdateNotifyLabel.setVisibility(View.VISIBLE);
                    mViewHolder.mSummaryUpdateButton.setVisibility(View.VISIBLE);
                }else{
                    mViewHolder.mSummaryUpdateNotifyLabel.setVisibility(View.INVISIBLE);
                    mViewHolder.mSummaryUpdateButton.setVisibility(View.GONE);
                }
            }
        };

        CallBackUtil<Object> callBackTopUpWebView = new CallBackUtil<Object>() {
            @Override
            public void eventCallBack(Object obj) {
                String webViewUrl = APIServices.P2P_BASE_URL +
                        "mobile2/top_up" +
                        "?lang=" + LocaleHelper.getLanguage(getActivity()) +
                        "&device_id=" +
                        ConstantVariables.getUniqueAndroidID(getActivity());

                Log.d(LOG_TAG, "APP Top Up Action webViewUrl " + webViewUrl);
                
                Intent intent = Henson.with(getActivity())
                        .gotoWebViewActivity()
                        .mUrl(webViewUrl)
                        .build();
                startActivityForResult(intent, INTENT_REQUEST_TOP_UP);
            }
        };

        CallBackUtil<Object> callBackConfirmBtnPress = new CallBackUtil<Object>() {
            @Override
            public void eventCallBack(Object obj) {
                final List<InvestBid> investBidList = mCheckoutSummaryAdapter.getInvestmentBidList();
                if(investBidList.isEmpty()){
                    SnackBarUtil.snackBarForInfoCreate(getView(),
                            mCheckoutSummaryEmptyMessage, Snackbar.LENGTH_SHORT)
                            .show();
                }else{
                    new MaterialDialog.Builder(mContext)
                            .autoDismiss(false)
                            .buttonsGravity(GravityEnum.END)
                            .content(R.string.checkout_summary_dialog_message_label)
                            .positiveText(R.string.checkout_summary_dialog_agree_label)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    checkoutConfirmProcess(investBidList, dialog);
                                }
                            })
                            .negativeText(R.string.checkout_summary_dialog_cancel_label)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .neutralText(R.string.checkout_summary_dialog_download_agreement_label)
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    downloadParticipationAgreement(dialog);
                                }
                            })
                            .show();

                }

            }
        };

        this.mViewHolder = new CheckoutSummaryViewHolder(rootView, getActivity(),
                callBackSummaryRefresh, callBackTopUpWebView, callBackConfirmBtnPress);

        mViewHolder.initView();

        this.mCheckoutSummaryAdapter = new CheckoutSummaryAdapter(getActivity(),
                mCheckoutSummaryRecyclerView, callBackSummaryRefresh, callBackShowUpdateBtn);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mCheckoutSummaryRecyclerView.setLayoutManager(mLayoutManager);
        mCheckoutSummaryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCheckoutSummaryRecyclerView.setAdapter(this.mCheckoutSummaryAdapter);

        mViewHolder.mSummaryCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        //get touch listener to dictate after action
        ItemTouchHelperExtension.Callback itemTouchCallback =
                new CheckoutSummaryItemTouchCallback(getActivity());
        mItemTouchHelper = new ItemTouchHelperExtension(itemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mCheckoutSummaryRecyclerView);

        mViewHolder.mSummaryUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSummaryList();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateSummaryList(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(disposableGetCheckoutSummary != null){
            disposableGetCheckoutSummary.dispose();
        }
        if(disposablePostCheckoutUpdate != null){
            disposablePostCheckoutUpdate.dispose();
        }
        if(disposablePostCheckoutConfirm != null){
            disposablePostCheckoutConfirm.dispose();
        }

        //dispose inside adapter
        if(mCheckoutSummaryAdapter != null){
            mCheckoutSummaryAdapter.removeDisposablePostDeleteBid();
        }
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "APP onActivityResult");
        if(requestCode == INTENT_REQUEST_TOP_UP){
            //if coming back from WebView, do refresh
            populateSummaryList(true);
        }
    }

    private void populateSummaryList(final boolean doRefreshList){

        Log.d(LOG_TAG, "APP populateSummaryList()");

        //check authentication
        CheckoutClient.getInstance(getActivity())
                .getCheckoutSummary(ConstantVariables.getUniqueAndroidID(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CheckoutSummaryResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableGetCheckoutSummary = d;
                    }

                    @Override
                    public void onNext(@NonNull Response<CheckoutSummaryResponse> response) {
                        if(response.isSuccessful()) {
                            CheckoutSummaryResponse body = response.body();
                            if (doRefreshList){
                                List<Investment> investments = body.getBids();
                                List<Loan> loans = body.getLoans();
                                mCheckoutSummaryAdapter.setBiddingInvestmentsAndLoans(investments, loans);
                            }
                            mViewHolder.populateSummaryDetails(body.getTotalPendingBids(), body.getAvailableCashBalance());
                        }else{
                            Log.d(LOG_TAG, "APP getCheckoutSummary !isSuccessful onNext() status > " + response.code());
                            if (ConstantVariables.HTTP_UNAUTHORISED == response.code()) {
                                AuthAccountUtils.actionLogout(getActivity());
                            } else {
                                //all other 4xx codes
                                String serverErrorMessage = HTTPResponseUtils
                                        .errorServerResponseConvert(checkoutClient,
                                                response.errorBody());

                                SnackBarUtil.snackBarForErrorCreate(getView(),
                                        serverErrorMessage, Snackbar.LENGTH_SHORT)
                                        .show();
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
                        Log.d(LOG_TAG, "APP getCheckoutSummary onComplete()");
                    }
                });

    }

    private void updateSummaryList(){

        List<InvestBid> updateBidList = mCheckoutSummaryAdapter.getUpdateBidList();
        if(!updateBidList.isEmpty()) {
            checkoutClient = CheckoutClient.getInstance(getActivity());

            checkoutClient.postCheckoutUpdate(updateBidList, ConstantVariables.getUniqueAndroidID(mContext))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<CheckoutUpdateResponse>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            disposablePostCheckoutUpdate = d;
                        }

                        @Override
                        public void onNext(@NonNull Response<CheckoutUpdateResponse> response) {
                            Log.d(LOG_TAG, "APP updateSummaryList response: " + response.message());
                            if(response.isSuccessful()){
                                CheckoutUpdateResponse updateResponse = response.body();
                                if(updateResponse != null){
                                    Log.d(LOG_TAG, "APP updateSummaryList Rx response: "
                                            + updateResponse.getServerResponse().getMessage());
                                    Snackbar snackbar = SnackBarUtil.snackBarForInfoCreate(getView(),
                                            updateResponse.getServerResponse().getMessage(),
                                            Snackbar.LENGTH_SHORT);
                                    snackbar.addCallback(new Snackbar.Callback(){
                                        @Override
                                        public void onShown(Snackbar sb) {
                                            //refresh list when snackbar is displayed
                                            populateSummaryList(true);
                                        }
                                    }).show();

                                }
                            }else{
                                if (ConstantVariables.HTTP_UNAUTHORISED == response.code()) {
                                    AuthAccountUtils.actionLogout(getActivity());
                                } else {
                                    //all other 4xx codes
                                    String serverErrorMessage = HTTPResponseUtils
                                            .errorServerResponseConvert(checkoutClient,
                                                    response.errorBody());

                                    SnackBarUtil.snackBarForErrorCreate(getView(),
                                            serverErrorMessage, Snackbar.LENGTH_SHORT)
                                            .show();
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
                            Log.d(LOG_TAG, "APP updateSummaryList Rx onComplete");
                        }
                    });
        }
    }

    private void checkoutConfirmProcess(List<InvestBid> investBidList, final MaterialDialog dialog){
        Log.d(LOG_TAG, "APP checkoutConfirmProcess");

        if(!investBidList.isEmpty()){
            final ProgressDialog progress = new ProgressDialog(getActivity());
            progress.setMessage(mLabelWaitMessage);
            progress.setCancelable(false);
            progress.show();

            checkoutClient = CheckoutClient.getInstance(getActivity());

            checkoutClient.postCheckoutConfirm(investBidList,
                    ConstantVariables.getUniqueAndroidID(mContext))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<MessageResponse>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            disposablePostCheckoutConfirm = d;
                        }

                        @Override
                        public void onNext(@NonNull Response<MessageResponse> response) {
                            progress.dismiss();

                            if(response.isSuccessful()){
                                MessageResponse messageResponse = response.body();
                                String confirmResponseMessage = messageResponse.getServerResponse().getMessage();

                                SnackBarUtil.snackBarForInfoCreate(getView(),
                                        confirmResponseMessage, Snackbar.LENGTH_SHORT)
                                        .addCallback(new Snackbar.Callback(){
                                            @Override
                                            public void onDismissed(Snackbar snackbar, int event) {
                                                getActivity().getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.checkout_summary_content, new CheckoutConfirmFragment())
                                                        .commit();
                                            }
                                        }).show();
                            }else{
                                if (ConstantVariables.HTTP_UNAUTHORISED == response.code()) {
                                    AuthAccountUtils.actionLogout(getActivity());
                                } else {
                                    //all other 4xx codes
                                    String serverErrorMessage = HTTPResponseUtils
                                            .errorServerResponseConvert(checkoutClient,
                                                    response.errorBody());

                                    SnackBarUtil.snackBarForErrorCreate(getView(),
                                            serverErrorMessage, Snackbar.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            Log.d(LOG_TAG, "APP checkoutConfirmProcess");
                            dialog.dismiss();
                        }
                    });
        }
    }

    private void downloadParticipationAgreement(final MaterialDialog dialog){

        //check permissions
        if(PermissionsUtils.checkPermissionAndRequestActivity(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ConstantVariables.REQUEST_CODE_PERMISSIONS_WRITE_EXTERNAL_STORAGE) == false){
            return;
        }

        String mimeType = null;
        final String extension = MimeTypeMap.getFileExtensionFromUrl(ConstantVariables.PARTICIPATION_AGREEMENT_URL);
        if(extension != null) {
            mimeType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(extension);
        }

        Toast.makeText(getActivity(), mLabelDownloading,
                Toast.LENGTH_SHORT).show();

        final String usageMimeType = mimeType;

        RxDownloader.getInstance(getActivity())
                .download(ConstantVariables.PARTICIPATION_AGREEMENT_URL,
                        ConstantVariables.PARTICIPATION_AGREEMENT_FILENAME,
                        usageMimeType)
                .subscribe(new rx.Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "APP onCompleted Participation Agreement Download");
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "ERROR: onError " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(final String location) {
                        final Snackbar snackbar = SnackBarUtil
                                .snackBarForInfoCreate(getView(),
                                        mLabelDownloadedTo + location,
                                        Snackbar.LENGTH_LONG);

                        snackbar.setAction(mLabelOpen, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    File downloadFile = new File(new URI(location));
                                    intent.setData(Uri.fromFile(downloadFile));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    Intent chooserIntent = Intent.createChooser(intent, mLabelIntentFile);


                                    startActivity(chooserIntent);
                                }catch(URISyntaxException ue){
                                    Log.e(LOG_TAG, "ERROR: " + ue.getMessage(), ue);
                                    final Snackbar snackbar = SnackBarUtil.snackBarForErrorCreate(getView(),
                                            mLabelErrorOpenFile,
                                            Snackbar.LENGTH_LONG);

                                    snackbar.setAction(mLabelOkay, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            snackbar.dismiss();
                                        }
                                    });
                                }catch (ActivityNotFoundException anfe){
                                    Log.e(LOG_TAG, "ERROR: " + anfe.getMessage(), anfe);
                                    final Snackbar snackbar = SnackBarUtil.snackBarForErrorCreate(
                                            getView(), mLabelErrorOpenFile,
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
