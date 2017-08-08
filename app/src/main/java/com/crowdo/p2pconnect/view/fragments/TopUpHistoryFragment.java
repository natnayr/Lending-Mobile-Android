package com.crowdo.p2pconnect.view.fragments;

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
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.model.response.MessageResponse;
import com.crowdo.p2pconnect.model.response.TopUpHistoryResponse;
import com.crowdo.p2pconnect.view.adapters.TopUpHistoryAdapter;
import com.esafirm.rxdownloader.RxDownloader;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;

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

        WalletClient.getInstance(getActivity())
                .getTopUpHistory(ConstantVariables.getUniqueAndroidID(getActivity()))
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
                                    .setTopUpHistoryList(topUpHistoryResponse.getTopUps());
                        }else {
                            Log.d(LOG_TAG, "APP getTopUpHistory onNext() status > " + response.code());
                            if(HTTPResponseUtils.check4xxClientError(response.code())){
                                String serverErrorMsg = "Error: Getting Top Up History not successful";
                                if(response.errorBody() != null){
                                    Converter<ResponseBody, MessageResponse> errorConverter =
                                            WalletClient.getInstance(getActivity()).getRetrofit().responseBodyConverter(
                                                    MessageResponse.class, new Annotation[0]);
                                    try{
                                        MessageResponse errorResponse = errorConverter.convert(response.errorBody());
                                        serverErrorMsg = errorResponse.getServer().getMessage();
                                    }catch (IOException e){
                                        e.printStackTrace();
                                        Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
                                    }
                                }

                                SnackBarUtil.snackBarForWarningCreate(getView(), serverErrorMsg,
                                        Snackbar.LENGTH_SHORT).show();                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP populateTopUpHistory onComplete()");
                    }
                });
    }

    public void downloadProof(String url){
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

        RxDownloader.getInstance(getActivity())
                .download(url, fileName, mimeType)
                .subscribe(new rx.Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

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
    public void onDestroy() {
        if(disposableGetTopUpHistory != null){
            if(!disposableGetTopUpHistory.isDisposed()){
                disposableGetTopUpHistory.dispose();
            }
        }
        super.onDestroy();
    }
}
