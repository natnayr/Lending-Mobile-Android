package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.crowdo.p2pmobile.data.LoanItem;
import com.crowdo.p2pmobile.data.LoanListClient;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by cwdsg05 on 8/11/16.
 */

public class LoansListFragment extends Fragment {

    private static final String LOG_TAG = LoansListFragment.class.getSimpleName();

    private ListView mListView;
    private LoansAdapter mLoanAdapter;
    private Subscription subscription;

    public LoansListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoanAdapter = new LoansAdapter(getActivity());
        populateLoansList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listview_loans);

        mListView.setAdapter(mLoanAdapter);
        Log.d(LOG_TAG, "TEST: onCreateView setAdapter");


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long l) {
                Toast.makeText(getActivity(), "hello, im at position " + position, Toast.LENGTH_LONG).show();
                LoanItem item = (LoanItem) adapterView.getItemAtPosition(position);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        super.onDestroy();
    }


    private void populateLoansList(){
        subscription = LoanListClient.getInstance()
                .getLiveLoans()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LoanItem>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "TEST: populateLoansList Rx onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "ERROR: " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<LoanItem> loanItems) {
                        Log.d(LOG_TAG, "TEST: populateLoansList Rx onNext with "
                                + loanItems.size() + " items retreived.");
                        mLoanAdapter.setLoans(loanItems);
                    }
                });

    }

}
