package com.crowdo.p2pconnect.view.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.WalletClient;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.model.response.TopUpSubmitResponse;
import com.crowdo.p2pconnect.support.DefinitionsRetrieval;
import com.crowdo.p2pconnect.support.MemberInfoRetrieval;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.model.response.DefinitionBankInfoResponse;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
import com.crowdo.p2pconnect.view.activities.TopUpActivity;
import com.crowdo.p2pconnect.viewholders.TopUpSubmitViewHolder;
import com.github.angads25.filepicker.controller.DialogSelectionListener;

import java.io.File;
import java.util.Arrays;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 13/7/17.
 */

public class TopUpSubmitFragment extends Fragment{

    private TopUpSubmitViewHolder viewHolder;
    private File chosenFile;
    private Disposable postTopUpInitDisposable;
    private Disposable putTopUpUploadDisposable;
    private static final String LOG_TAG = TopUpSubmitFragment.class.getSimpleName();

    public TopUpSubmitFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_top_up_submit, container, false);

        viewHolder = new TopUpSubmitViewHolder(rootView, getActivity());
        viewHolder.initView();

        MemberInfoRetrieval memberRetrieval = new MemberInfoRetrieval();
        memberRetrieval.retrieveInfo(getActivity(), new CallBackUtil<MemberInfoResponse>() {
            @Override
            public void eventCallBack(MemberInfoResponse memberInfoResponse) {
                viewHolder.fillMemberInfo(memberInfoResponse);
            }
        });

        DefinitionsRetrieval definitionsRetrieval = new DefinitionsRetrieval();
        definitionsRetrieval.retreiveInfo(getActivity(), new CallBackUtil<DefinitionBankInfoResponse>() {
            @Override
            public void eventCallBack(DefinitionBankInfoResponse definitionBankInfoResponse) {
                viewHolder.fillDefinitionsBankInfo(definitionBankInfoResponse);
            }
        });

        SoftInputHelper.setupUI(rootView, getActivity(), new EditText[]{viewHolder.mSubmiUploadReferenceEditText});
        viewHolder.mSubmiUploadReferenceEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int key, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (key) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_NUMPAD_ENTER:
                            SoftInputHelper.hideSoftKeyboardOfView(view,view);
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        viewHolder.mSubmiUploadReferenceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    ((TopUpActivity) getActivity()).mTopUpBalanceExpandableLayout.collapse();
                }
            }
        });

        viewHolder.pickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                if(files.length == 1){
                    //force single file only
                    chosenFile = new File(files[0]);
                    viewHolder.mSubmitUploadOpenDialogInstructionsMainTextView.setTypeface(null,
                            Typeface.BOLD_ITALIC);
                    viewHolder.mSubmitUploadOpenDialogInstructionsMainTextView.setText(chosenFile.getName());
                }
            }
        });

        viewHolder.mSubmitUploadSubloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar invalidFileSnackbar = SnackBarUtil.snackBarForWarningCreate(rootView, getResources()
                                .getString(R.string.top_up_submit_upload_attach_file_warning), Snackbar.LENGTH_SHORT);
                if(chosenFile == null){
                    invalidFileSnackbar.show();
                    return;
                }

                String[] allowedExtensions =  new String[]{"pdf","doc","docx","jpeg", "jpg","png",
                        "tif","bmp"};
                final String fileUploadExtension = MimeTypeMap.getFileExtensionFromUrl(chosenFile.getAbsolutePath());
                if(fileUploadExtension == null){
                    invalidFileSnackbar.show();
                    return;
                }
                if(!Arrays.asList(allowedExtensions).contains(fileUploadExtension)){
                    //not within allowed filetypes
                    invalidFileSnackbar.show();
                    return;
                }

                final String fileUploadType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileUploadExtension);
                String refereceText = viewHolder.mSubmiUploadReferenceEditText.getText().toString();

                Log.d(LOG_TAG, "APP uploading file:" + chosenFile.getName() + " type:" + fileUploadType);
                Log.d(LOG_TAG, "APP attaching referenceText: " + refereceText);

                final WalletClient walletClient = WalletClient.getInstance(getActivity());

                walletClient.postTopUpInit(refereceText, ConstantVariables.getUniqueAndroidID(getActivity()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<TopUpSubmitResponse>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                postTopUpInitDisposable = d;
                            }

                            @Override
                            public void onNext(Response<TopUpSubmitResponse> response) {
                                if(response.isSuccessful()){
                                    TopUpSubmitResponse topUpSubmitResponse = response.body();

//                                    uploadFileToServer(chosenFile, fileUploadType,
//                                            topUpSubmitResponse.getTopUp().getId());
                                    Log.d(LOG_TAG, "APP attaching topupId: " + topUpSubmitResponse.getTopUp().getId());
                                }else{
                                    if (HTTPResponseUtils.check4xxClientError(response.code())){
                                        //all other 4xx codes
                                        String serverErrorMessage = HTTPResponseUtils
                                                .errorServerResponseConvert(walletClient,
                                                        response.errorBody());

                                        SnackBarUtil.snackBarForWarningCreate(getView(),
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
                                Log.d(LOG_TAG, "APP: postTopUpInit onComplete");
                            }
                        });


            }
        });

        return rootView;
    }

    private void uploadFileToServer(final File fileUpload, final String mediaType, final long topUpId){

        final WalletClient walletClient = WalletClient.getInstance(getActivity());

        walletClient.putTopUpUpload(fileUpload, mediaType, topUpId,
                ConstantVariables.getUniqueAndroidID(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<TopUpSubmitResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        putTopUpUploadDisposable = d;
                    }

                    @Override
                    public void onNext(Response<TopUpSubmitResponse> response) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onPause() {
        if(postTopUpInitDisposable != null){
            if(!postTopUpInitDisposable.isDisposed()) {
                postTopUpInitDisposable.dispose();
            }
        }
        if(putTopUpUploadDisposable != null){
            if(!putTopUpUploadDisposable.isDisposed()){
                putTopUpUploadDisposable.dispose();
            }
        }
        super.onPause();
    }
}
