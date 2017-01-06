package com.crowdo.p2pmobile.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdo.p2pmobile.activity.Henson;
import com.crowdo.p2pmobile.activity.LoanDetailsActivity;
import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.data.LoanDetail;
import com.crowdo.p2pmobile.data.LoanDetailClient;
import com.crowdo.p2pmobile.data.LoanFactSheetClient;
import com.crowdo.p2pmobile.data.RegisteredMemberCheck;
import com.crowdo.p2pmobile.data.RegisteredMemberCheckClient;
import com.crowdo.p2pmobile.helper.PerformEmailIdentityCheckTemp;
import com.crowdo.p2pmobile.helper.SharedPreferencesHelper;
import com.crowdo.p2pmobile.viewholder.LoanDetailsViewHolder;

import java.io.File;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoanDetailsFragment extends Fragment {

    private static final String LOG_TAG = LoanDetailsFragment.class.getSimpleName();
    private Subscription detailsSubscription;
    private Subscription factsheetSubscription;
    private int initId;
    private LoanDetailsViewHolder viewHolder;
    private AlertDialog alertDialog;

    public LoanDetailsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments()
                .getInt(LoanDetailsActivity.BUNDLE_ID_KEY) >= 0 ) {
            this.initId = getArguments()
                    .getInt(LoanDetailsActivity.BUNDLE_ID_KEY); //store
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, parent, false);

        viewHolder = new LoanDetailsViewHolder(rootView);

        //Init view first,
        viewHolder.initView(getActivity(), this.initId);

        detailsSubscription = LoanDetailClient.getInstance()
                .getLoanDetails(this.initId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoanDetail>() {

                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "Populated LoanDetail Rx onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(LoanDetail loanDetail) {
                        if(loanDetail != null) {
                            Log.d(LOG_TAG, "Populated LoanDetails Rx onNext with loanId "
                                    + loanDetail.loanId + " retreived.");
                            viewHolder.attachView(loanDetail, getActivity());
                        }
                    }
                });

        //set file download button here..
        viewHolder.mFactsheetDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(initId >= 0) {
                    Toast.makeText(getActivity(),
                            "Downloading...",
                            Toast.LENGTH_SHORT).show();

                    factsheetSubscription = LoanFactSheetClient.getInstance(getActivity(), initId)
                        .getLoanFactSheet()
                        .subscribe(new Observer<File>() {
                            @Override
                            public void onCompleted() {
                                Log.d(LOG_TAG, "TEST: mFactSheetDownloadBtn complete");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(LOG_TAG, "ERROR: onError " + e.getMessage(), e);
                            }

                            @Override
                            public void onNext(File file) {
                                Toast.makeText(getActivity(),
                                        "factsheet stored in app cache as: "
                                                + file.getName(),
                                        Toast.LENGTH_SHORT).show();

                                Log.d(LOG_TAG, "TEST: mFactSheetDownloadBtn onNext => "
                                    + file.getAbsolutePath());

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                                        | Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Intent chooserIntent = Intent.createChooser(intent, "Open With");

                                try{
                                    startActivity(chooserIntent);
                                }catch (ActivityNotFoundException e){
                                    Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);

                                    Toast.makeText(getActivity(),
                                            "Sorry, you do not seem to have a PDF Reader installed, " +
                                                    "moving file to downloads",
                                            Toast.LENGTH_LONG).show();

                                    file.renameTo(new File(Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile(),
                                            file.getName()));
                                }
                            }
                        });
                }
            }
        });

        rootView.setTag(viewHolder);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewHolder != null) {

            viewHolder.mBidEnterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int acctMemberId = SharedPreferencesHelper.getSharedPrefInt(getActivity(),
                            getActivity().getString(R.string.pref_user_id_key), -1);

                    Log.d(LOG_TAG, "TEST: acctMemberId=" + acctMemberId);

                    if(acctMemberId == -1) {
                        dialogEmailPrompt();
                    }else {
                        Intent intent = Henson.with(getActivity())
                                .gotoWebViewActivity()
                                .id(initId)
                                .url("http://p2p.crowdo.com")
                                .build();

                        startActivity(intent);
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
        super.onDestroy();
    }

    /*
        Temp Dialog to identify user
     */
    @BindView(android.R.id.message) TextView memberCheckEmailTextView;
    @BindView(android.R.id.edit) EditText memberCheckEmailEditText;
    @BindString(R.string.member_check_email_dialog_label) String memberCheckEmailDialogLabel;
    @BindString(R.string.pref_user_email_default_value) String memberCheckEmailDialogDefaultValue;
    private void dialogEmailPrompt(){

        Log.d(LOG_TAG, "TEST: email dialog pop up start");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.pref_dialog_email_edittext_fix, null);
        ButterKnife.bind(this, dialogView);

        // setting dialog layout
        memberCheckEmailTextView.setText(memberCheckEmailDialogLabel);

        AlertDialog.Builder alertDialogBuilderInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderInput.setView(dialogView);

        alertDialogBuilderInput
                .setCancelable(false)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                        final String enteredEmail = memberCheckEmailEditText.getText()
                                .toString().toLowerCase().trim();

                        Call<RegisteredMemberCheck> call = RegisteredMemberCheckClient.getInstance()
                                .postUserCheck(enteredEmail);

                        call.enqueue(new Callback<RegisteredMemberCheck>() {
                            @Override
                            public void onResponse(Call<RegisteredMemberCheck> call,
                                                   Response<RegisteredMemberCheck> response) {

                                PerformEmailIdentityCheckTemp idenCheck =
                                        new PerformEmailIdentityCheckTemp(getActivity());
                                idenCheck.onResponseCode(LOG_TAG, enteredEmail, response);
                            }

                            @Override
                            public void onFailure(Call<RegisteredMemberCheck> call, Throwable t) {
                                PerformEmailIdentityCheckTemp idenCheck =
                                        new PerformEmailIdentityCheckTemp(getActivity());
                                idenCheck.onFailure(LOG_TAG, enteredEmail, t);
                            }
                        });

                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton("Proceed as Guest", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.cancel();
                    }
                });

        alertDialog = alertDialogBuilderInput.create();
        alertDialog.show();

    }

}
