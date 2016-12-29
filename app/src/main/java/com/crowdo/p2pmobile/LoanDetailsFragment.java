package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crowdo.p2pmobile.data.LoanDetail;
import com.crowdo.p2pmobile.data.LoanDetailClient;
import com.crowdo.p2pmobile.data.LoanFactSheetClient;
import com.crowdo.p2pmobile.viewholders.LoanDetailsViewHolder;

import java.io.File;

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

        final LoanDetailsViewHolder viewHolder = new LoanDetailsViewHolder(rootView);

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
                            }

                            @Override
                            public void onNext(File file) {
                                Toast.makeText(getActivity(),
                                        "FactSheet completed downloading into "
                                                + file.getAbsolutePath(),
                                        Toast.LENGTH_LONG).show();

                                Log.d(LOG_TAG, "TEST: mFactSheetDownloadBtn onNext => "
                                    + file.getAbsolutePath());

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                Intent chooserIntent = Intent.createChooser(intent, "Open With");

                                try{
                                    startActivity(chooserIntent);
                                }catch (ActivityNotFoundException e){
                                    Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);

                                    Toast.makeText(getActivity(),
                                            "Sorry, you do not seem to have a PDF Reader installed..",
                                            Toast.LENGTH_LONG).show();
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
    public void onDestroy() {
        if(detailsSubscription != null && !detailsSubscription.isUnsubscribed()){
            detailsSubscription.unsubscribe();
        }
        if(factsheetSubscription != null && !factsheetSubscription.isUnsubscribed()){
            factsheetSubscription.unsubscribe();
        }
        super.onDestroy();
    }

}
