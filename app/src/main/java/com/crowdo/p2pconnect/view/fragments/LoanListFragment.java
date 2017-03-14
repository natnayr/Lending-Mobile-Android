package com.crowdo.p2pconnect.view.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.view.activities.Henson;
import com.crowdo.p2pconnect.model.LoanListItem;
import com.crowdo.p2pconnect.data.LoanListClient;
import com.crowdo.p2pconnect.view.adapters.LoanListAdapter;
import com.crowdo.p2pconnect.viewholders.LoanListFilterViewHolder;

import net.cachapa.expandablelayout.ExpandableLayout;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.loan_list_view_filtering_expandable) ExpandableLayout loanListSearchExpandableLayout;
    @BindView(R.id.listview_loans) ListView mListView;
    @BindView(R.id.loan_list_view_swipe) SwipeRefreshLayout swipeContainer;

    @BindView(R.id.loan_list_view_filtering_hide_button) LinearLayout filteringHideButton;
    @BindView(R.id.loan_list_view_filtering_clear_button) LinearLayout filteringClearButton;

    @BindView(R.id.loan_list_view_filtering_count) TextView filteringCountLabel;
    @BindString(R.string.loan_list_action_filter_item_count_tail) String filteringCountTail;

    private LoanListAdapter mLoanAdapter;
    private Subscription loanListSubscription;
    private LoanListFilterViewHolder filteringViewHolder;
    private SearchView searchView;

    public LoanListFragment() {
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
        ButterKnife.bind(this, rootView);

        // use view holder
        filteringViewHolder = new LoanListFilterViewHolder(rootView);
        filteringViewHolder.initView(mLoanAdapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateLoansList();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.color_primary_700,
                R.color.color_primary, R.color.color_primary_300);

        mListView.setAdapter(mLoanAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long l) {

                LoanListItem item = (LoanListItem) adapterView.getItemAtPosition(position);
                Intent intent = Henson.with(getActivity())
                        .gotoLoanDetailsActivity()
                        .id(item.getId())
                        .build();
                startActivity(intent);
            }
        });

        filteringHideButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loanListSearchExpandableLayout.collapse();
                searchView.clearFocus();
            }
        });

        filteringClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteringViewHolder.clearFiltering();
            }
        });

        mLoanAdapter.setFilteringCountTextView(filteringCountLabel, filteringCountTail);

        loanListSearchExpandableLayout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction) {
                //set alpha and enabled of ListView
                mListView.setAlpha(1 - (expansionFraction * ((float) 0.8)));
            }
        });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (loanListSearchExpandableLayout.isExpanded()) {
                        loanListSearchExpandableLayout.collapse();
                        return true;
                    }
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SoftInputHelper.hideSoftKeyboard(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        if (loanListSubscription != null &&
                !loanListSubscription.isUnsubscribed()) {
            loanListSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(LOG_TAG, "APP: inflating SearchView");
        menu.clear();

        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_search_loans);
        if(menuItem != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

            searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(loanListSearchExpandableLayout != null){
                        if(loanListSearchExpandableLayout.isExpanded()){
                            loanListSearchExpandableLayout.collapse();
                        }
                    }
                    mLoanAdapter.searchLoans();
                    searchView.clearFocus();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mLoanAdapter.setSearchQuery(newText);
                    mLoanAdapter.searchLoans();
                    return false;
                }
            });

            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        setSearchExpandedLayoutExpand();
                    }
                }
            });

            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSearchExpandedLayoutExpand();
                    //remove initial onFocus
                    if(searchView.hasFocus()) {
                        searchView.clearFocus();
                    }
                }
            });
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    setSearchExpandedLayoutCollapse();
                    return false;
                }
            });

            searchView.setQueryHint(getString(R.string.loan_list_action_search));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_loans:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateLoansList() {
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

    private void setSearchExpandedLayoutCollapse(){
        if(loanListSearchExpandableLayout != null){
            if(loanListSearchExpandableLayout.isExpanded()){
                Log.d(LOG_TAG, "APP: loanListSearchExpandableLayout is collapsing.");
                loanListSearchExpandableLayout.collapse();
            }
        }
    }

    private void setSearchExpandedLayoutExpand(){
        if(loanListSearchExpandableLayout != null){
            if(!loanListSearchExpandableLayout.isExpanded()){
                Log.d(LOG_TAG, "APP: loanListSearchExpandableLayout is expanding.");
                loanListSearchExpandableLayout.expand();
            }
        }
    }





}