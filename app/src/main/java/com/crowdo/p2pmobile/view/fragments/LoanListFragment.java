package com.crowdo.p2pmobile.view.fragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.helpers.SoftInputHelper;
import com.crowdo.p2pmobile.view.activities.Henson;
import com.crowdo.p2pmobile.model.LoanListItem;
import com.crowdo.p2pmobile.data.LoanListClient;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cwdsg05 on 8/11/16.
 */

public class LoanListFragment extends Fragment {

    private static final String LOG_TAG = LoanListFragment.class.getSimpleName();

    public static final String TAG_LOAN_LIST_FRAGMENT = "LOAN_LIST_FRAGMENT_TAG";
    private ListView mListView;
    private LoanListAdapter mLoanAdapter;
    private Subscription loanListSubscription;
    private SwipeRefreshLayout swipeContainer;

    public LoanListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoanAdapter = new LoanListAdapter(getActivity());
        populateLoansList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_loan_list, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listview_loans);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLoanListView);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateLoansList();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.color_primary_dark,
                R.color.color_primary, R.color.color_primary_light);

        mListView.setAdapter(mLoanAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long l) {

                LoanListItem item = (LoanListItem) adapterView.getItemAtPosition(position);
                Intent intent = Henson.with(getActivity())
                        .gotoLoanDetailsActivity()
                        .id(item.id)
                        .build();
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SoftInputHelper.hideSoftKeyboard(getActivity());
    }

    @Override
    public void onDestroy() {
        if(loanListSubscription != null &&
                !loanListSubscription.isUnsubscribed()){
            loanListSubscription.unsubscribe();
        }

        super.onDestroy();
    }

    private void populateLoansList(){
        loanListSubscription = LoanListClient.getInstance()
                .getLiveLoans()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LoanListItem>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(LOG_TAG, "APP: populateLoansList Rx onComplete");
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<LoanListItem> loanListItems) {
                        Log.d(LOG_TAG, "APP: populateLoansList Rx onNext with "
                                + loanListItems.size() + " items retreived.");
                        mLoanAdapter.setLoans(loanListItems);
                    }
                });
    }
    
}
