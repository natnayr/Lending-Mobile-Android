package com.crowdo.p2pconnect.view.fragments;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.WalletClient;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.PermissionsUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.model.response.TopUpHistoryResponse;
import com.crowdo.p2pconnect.support.InvestorAccreditationReaction;
import com.crowdo.p2pconnect.view.adapters.TopUpHistoryAdapter;
import com.esafirm.rxdownloader.RxDownloader;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 13/7/17.
 */

public class TopUpHistoryFragment extends Fragment {

    @BindView(R.id.top_up_history_recycler_view) RecyclerView mTopUpHistoryRecyclerView;
    @BindString(R.string.downloaded_to_label) String mDownloadedTo;
    @BindString(R.string.downloading_label) String mDownloading;
    @BindString(R.string.open_label) String mOpen;
    @BindString(R.string.okay_label) String mOkay;
    @BindString(R.string.intent_file_chooser) String mOpenWith;
    @BindString(R.string.unable_open_file_label) String mErrorOpenFile;
    @BindString(R.string.top_up_invalid_investor_label) String mInvalidInvestorLabel;
    @BindString(R.string.top_up_invalid_investor_button_label) String mInvalidInvestorButtonLabel;


    private static final String LOG_TAG = TopUpHistoryFragment.class.getSimpleName();
    private TopUpHistoryAdapter mTopUpHistoryAdapter;
    private Disposable disposableGetTopUpHistory;

    public TopUpHistoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_up_history, container, false);
        ButterKnife.bind(this, rootView);

        CallBackUtil<String> callBackDownloadProof = new CallBackUtil<String>() {
            @Override
            public void eventCallBack(String url) {
                downloadProof(url);
            }
        };


        this.mTopUpHistoryAdapter = new TopUpHistoryAdapter(getActivity(), callBackDownloadProof);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mTopUpHistoryRecyclerView.setLayoutManager(mLayoutManager);
        mTopUpHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTopUpHistoryRecyclerView.setAdapter(this.mTopUpHistoryAdapter);


        populateTopUpHistory();

        return rootView;
    }

    public void populateTopUpHistory(){
        Log.d(LOG_TAG, "APP populateTopUpHistory");

        final WalletClient walletClient = WalletClient.getInstance(getActivity());

        walletClient.getTopUpHistory(ConstantVariables.getUniqueAndroidID(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<TopUpHistoryResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableGetTopUpHistory = d;
                    }

                    @Override
                    public void onNext(Response<TopUpHistoryResponse> response) {
                        if(response.isSuccessful()){
                            TopUpHistoryResponse topUpHistoryResponse = response.body();

                            TopUpHistoryFragment.this.mTopUpHistoryAdapter
                                    .setWalletEntryHistoryList(topUpHistoryResponse.getWalletEntries());
                        }else {
                            Log.d(LOG_TAG, "APP getTopUpHistory onNext() status > " + response.code());
                            if(HTTPResponseUtils.check4xxClientError(response.code())){
                                if (ConstantVariables.HTTP_INVESTOR_FAILED_ACCREDITATION == response.code()){

                                    Snackbar investorInvalidSnackbar = InvestorAccreditationReaction
                                            .failedInvestorAcreditationSnackbar(
                                                    mInvalidInvestorLabel, mInvalidInvestorButtonLabel,
                                                    getView(), getActivity());
                                    investorInvalidSnackbar.show();
                                }else {
                                    String serverErrorMessage = HTTPResponseUtils
                                            .errorServerResponseConvert(walletClient,
                                                    response.errorBody());

                                    SnackBarUtil.snackBarForWarningCreate(getView(),
                                            serverErrorMessage, Snackbar.LENGTH_SHORT)
                                            .show();
                                }}
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP getTopUpHistory onComplete()");
                    }
                });
    }

    public void downloadProof(String url){

        //check permissions
        if(PermissionsUtils.checkPermissionAndRequestActivity(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ConstantVariables.REQUEST_CODE_PERMISSIONS_WRITE_EXTERNAL_STORAGE) == false){
            return;
        }

        String mimeType = null;
        final String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if(extension != null) {
            mimeType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(extension);
        }

        Toast.makeText(getActivity(), mDownloading,
                Toast.LENGTH_SHORT).show();

        final String usageMimeType = mimeType;
        final String fileName = URLUtil.guessFileName(url, null, null);

        Log.d(LOG_TAG, "APP downloading " + url + " filename:" + fileName + "/" + usageMimeType);


        RxDownloader.getInstance(getActivity())
                .download(url, fileName, usageMimeType)
                .subscribe(new rx.Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "APP onCompleted download proof");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(final String location) {
                        Log.d(LOG_TAG, "APP file is now in " + location);

                        final Snackbar snackbar = SnackBarUtil
                                .snackBarForSuccessCreate(getView(), mDownloadedTo + location,
                                        Snackbar.LENGTH_LONG)
                                .setAction(mOpen, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            File downloadFile = new File(new URI(location));

                                            intent.setDataAndType(Uri.fromFile(downloadFile), usageMimeType);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            Intent chooserIntent = Intent.createChooser(intent, mOpenWith);


                                            startActivity(chooserIntent);
                                        }catch(URISyntaxException ue){
                                            Log.e(LOG_TAG, "ERROR " + ue.getMessage(), ue);
                                            final Snackbar snackbar = SnackBarUtil
                                                    .snackBarForErrorCreate(getView(), mErrorOpenFile,
                                                            Snackbar.LENGTH_LONG);

                                            snackbar.setAction(mOkay, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    snackbar.dismiss();
                                                }
                                            });
                                        }catch (ActivityNotFoundException anfe){
                                            Log.e(LOG_TAG, "ERROR " + anfe.getMessage(), anfe);
                                            final Snackbar snackbar = SnackBarUtil.snackBarForErrorCreate(
                                                    getView(), mErrorOpenFile,
                                                    Snackbar.LENGTH_LONG);

                                            snackbar.setAction(mOkay, new View.OnClickListener() {
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

    public void refreshFragment(){
        Log.d(LOG_TAG, "APP refreshFragment called");
        FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
        ftr.detach(TopUpHistoryFragment.this);
        ftr.attach(TopUpHistoryFragment.this);
        ftr.commit();
    }

    @Override
    public void onPause() {
        if(disposableGetTopUpHistory != null){
            if(!disposableGetTopUpHistory.isDisposed()){
                disposableGetTopUpHistory.dispose();
            }
        }
        super.onPause();
    }


}
