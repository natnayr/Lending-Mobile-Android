package com.crowdo.p2pconnect.view.fragments;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.WalletClient;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.model.response.TopUpSubmitResponse;
import com.crowdo.p2pconnect.support.DefinitionsRetrieval;
import com.crowdo.p2pconnect.support.InvestorAccreditationReaction;
import com.crowdo.p2pconnect.support.MemberInfoRetrieval;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.model.response.DefinitionBankInfoResponse;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
import com.crowdo.p2pconnect.view.activities.TopUpActivity;
import com.crowdo.p2pconnect.viewholders.TopUpSubmitViewHolder;
import com.developers.imagezipper.ImageZipper;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import butterknife.BindString;
import butterknife.ButterKnife;
import de.mateware.snacky.Snacky;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 13/7/17.
 */

public class TopUpSubmitFragment extends Fragment{

    @BindString(R.string.top_up_invalid_investor_label) String mInvestorInvalidLabel;
    @BindString(R.string.top_up_invalid_investor_button_label) String mInvestorInvalidButtonLabel;

    private TopUpSubmitViewHolder viewHolder;
    private File chosenFile;
    private long chosenFileSizeKB;
    private Disposable postTopUpInitDisposable;
    private Disposable putTopUpUploadDisposable;
    private MaterialDialog waitForUpload;
    public FilePickerDialog filePickerDialog;

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
        ButterKnife.bind(this, rootView);

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

        if(viewHolder.pickerDialog != null) {
            this.filePickerDialog = viewHolder.pickerDialog;
        }

        viewHolder.pickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                if(files.length == 1){
                    //force single file only
                    chosenFile = new File(files[0]);
                    chosenFileSizeKB = chosenFile.length() / 1024; //in KB

                    //Special service, compress images (from camera perhaps?)
                    String[] imageExtensions =  new String[]{"jpeg", "jpg", "png", "tif","bmp"};

                    String fileExtension = MimeTypeMap.getFileExtensionFromUrl(chosenFile.getAbsolutePath());
                    if(Arrays.asList(imageExtensions).contains(fileExtension) &&
                            (chosenFileSizeKB/1024) > 2){
                        try {
                            File compressedFile = new ImageZipper(getActivity()).compressToFile(chosenFile);
                            chosenFileSizeKB = compressedFile.length() / 1024; //recalculate
                            chosenFile = compressedFile;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
                        }
                    }

                    NumberFormat formatter = new DecimalFormat("#0.00");
                    String outputMB = formatter.format(((double)chosenFileSizeKB)/1024);

                    viewHolder.mSubmitUploadOpenInstructionsMainTextView.setTypeface(null,
                            Typeface.BOLD_ITALIC);

                    viewHolder.mSubmitUploadOpenInstructionsMainTextView.setText(chosenFile.getName());

                    viewHolder.mSubmitUploadOpenInstructionsSubTextView.setTypeface(null,
                            Typeface.BOLD_ITALIC);


                    if((chosenFileSizeKB/1024) > 2){
                        //more than 2mb color red
                        viewHolder.mSubmitUploadOpenInstructionsSubTextView.setText(outputMB + "MB (file too big)");
                        viewHolder.mSubmitUploadOpenInstructionsSubTextView.setTextColor(viewHolder.mColorPrimary);
                    }else{
                        viewHolder.mSubmitUploadOpenInstructionsSubTextView.setText(outputMB + "MB");
                    }
                }
            }
        });

        viewHolder.mSubmitUploadSubloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar invalidFileSnackbar = SnackBarUtil.snackBarForWarningCreate(rootView, getResources()
                                .getString(R.string.top_up_submit_upload_attach_file_warning), Snackbar.LENGTH_SHORT);
                if(chosenFile == null ||  chosenFileSizeKB == 0){
                    invalidFileSnackbar.show();
                    return;
                }


                final String fileUploadExtension = MimeTypeMap.getFileExtensionFromUrl(chosenFile.getAbsolutePath());
                if(fileUploadExtension == null){
                    invalidFileSnackbar.show();
                    return;
                }
                String[] allowedExtensions =  new String[]{"pdf","doc","docx","jpeg", "jpg",
                        "png", "tif","bmp"};

                if(!Arrays.asList(allowedExtensions).contains(fileUploadExtension)){
                    //not within allowed filetypes
                    invalidFileSnackbar.show();
                    return;
                }

                if((chosenFileSizeKB/1024) > 2){
                    //under 2MB
                    invalidFileSnackbar.show();
                    return;
                }

                waitForUpload = new MaterialDialog.Builder(getActivity())
                        .content(R.string.wait_message)
                        .progress(true, 0)
                        .autoDismiss(false)
                        .show();

                final String fileUploadType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileUploadExtension);
                String refereceText = viewHolder.mSubmiUploadReferenceEditText.getText().toString();

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
                                    final TopUpSubmitResponse topUpSubmitResponse = response.body();

                                    uploadFileToServer(chosenFile, fileUploadType, topUpSubmitResponse.getTopUp().getId());

                                }else{
                                    waitForUpload.dismiss();
                                    if (HTTPResponseUtils.check4xxClientError(response.code())){
                                        if(ConstantVariables.HTTP_INVESTOR_FAILED_ACCREDITATION == response.code()){
                                            //option to signup given
                                            Snackbar investorInvalidSnackbar = InvestorAccreditationReaction
                                                    .failedInvestorAcreditationSnackbar(
                                                            mInvestorInvalidLabel, mInvestorInvalidButtonLabel,
                                                            getView(), getActivity());

                                            investorInvalidSnackbar.show();
                                        }else {
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
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
                                if(waitForUpload.isShowing()) waitForUpload.dismiss();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(LOG_TAG, "APP postTopUpInit onComplete");
                            }
                        });

            }
        });

        return rootView;
    }


    private void uploadFileToServer(final File fileUpload, final String mediaType, final long topUpId){

        Log.d(LOG_TAG, "APP uploadFileToServer = mediaType: " + mediaType);

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
                        waitForUpload.dismiss();
                        if(response.isSuccessful()){
                            viewHolder.initSubmitButtonState();

                            Spanned topUpStatement;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                topUpStatement = Html.fromHtml(viewHolder.mSubmitUploadSuccessLabel,
                                        Html.FROM_HTML_MODE_LEGACY);
                            } else {
                                topUpStatement = Html.fromHtml(viewHolder.mSubmitUploadSuccessLabel);
                            }

                            Snacky.builder().setView(getView())
                                    .setText(topUpStatement)
                                    .setDuration(Snackbar.LENGTH_SHORT)
                                    .success().show();

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
                        e.getMessage();
                        Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP onComplete");
                        if(waitForUpload.isShowing()) waitForUpload.dismiss();
                        ((TopUpActivity) getActivity()).refreshAllFragments();
                    }
                });
    }

    public void refreshFragment(){
        Log.d(LOG_TAG, "APP refreshFragment called");
        FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
        ftr.detach(TopUpSubmitFragment.this);
        ftr.attach(TopUpSubmitFragment.this);
        ftr.commit();
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
