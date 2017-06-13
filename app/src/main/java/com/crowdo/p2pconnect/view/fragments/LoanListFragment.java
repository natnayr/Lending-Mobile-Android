package com.crowdo.p2pconnect.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.LayerDrawable;
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
import com.crowdo.p2pconnect.commons.MemberDataRetrieval;
import com.crowdo.p2pconnect.custom_ui.CartBadgeDrawable;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.model.response.LoanListResponse;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
import com.crowdo.p2pconnect.oauth.AuthAccountUtils;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.oauth.CrowdoAccountGeneral;
import com.crowdo.p2pconnect.view.activities.CheckoutActivity;
import com.crowdo.p2pconnect.view.activities.Henson;
import com.crowdo.p2pconnect.data.client.LoanClient;
import com.crowdo.p2pconnect.view.adapters.LoanListAdapter;
import com.crowdo.p2pconnect.viewholders.LoanListFilterViewHolder;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 8/11/16.
 */

public class LoanListFragment extends Fragment {

    private static final String LOG_TAG = LoanListFragment.class.getSimpleName();
    public static final String LOAN_LIST_FRAGMENT_TAG = "LOAN_LIST_FRAGMENT_TAG";

    @BindView(R.id.loan_list_view_filtering_expandable) ExpandableLayout loanListSearchExpandableLayout;
    @BindView(R.id.listview_loans) ListView mListView;
    @BindView(R.id.loan_list_view_swipe) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.loan_list_view_filtering_hide_button) LinearLayout filteringHideButton;
    @BindView(R.id.loan_list_view_filtering_clear_button) LinearLayout filteringClearButton;
    @BindView(R.id.loan_list_view_filtering_count) TextView filteringCountLabel;
    @BindString(R.string.loan_list_action_filter_item_count_tail) String filteringCountTail;

    private LoanListAdapter mLoanAdapter;
    private LoanListFilterViewHolder filteringViewHolder;
    private SearchView searchView;
    private Disposable disposableGetLiveLoans;
    private Context mContext;
    private MenuItem mMenuCart;

    public LoanListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoanAdapter = new LoanListAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_loan_list, container, false);
        ButterKnife.bind(this, rootView);

        mContext = getActivity();

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

                Loan item = (Loan) adapterView.getItemAtPosition(position);
                Intent detailsIntent = Henson.with(getActivity())
                        .gotoLoanDetailsActivity()
                        .id(item.getId())
                        .build();
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(detailsIntent);
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
            public void onExpansionUpdate(float expansionFraction, int state) {
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
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
        populateLoansList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SoftInputHelper.hideSoftKeyboard(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(disposableGetLiveLoans != null){
            disposableGetLiveLoans.dispose();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        mMenuCart = menu.findItem(R.id.action_cart);
        updateShoppingCartItemCount();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.menu_search_and_cart, menu);
        final MenuItem menuSearch = menu.findItem(R.id.action_search_loans);

        mMenuCart = menu.findItem(R.id.action_cart);
        updateShoppingCartItemCount();

        if(menuSearch != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(menuSearch);

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

            searchView.setOnSearchClickListener(
            new View.OnClickListener() {
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

            Activity activity = getActivity();
            if(activity != null && isAdded()) {
                searchView.setQueryHint(getString(R.string.loan_list_action_search));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_loans:
                return true;
            case R.id.action_cart:
                Intent checkoutIntent = new Intent(getActivity(), CheckoutActivity.class);
                checkoutIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(checkoutIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateLoansList() {

        Log.d(LOG_TAG, "APP populateLoansList()");
        LoanClient.getInstance(getActivity())
                .getLiveLoans(ConstantVariables.getUniqueAndroidID(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<LoanListResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableGetLiveLoans = d;
                    }

                    @Override
                    public void onNext(Response<LoanListResponse> response) {
                        if (response.isSuccessful()) {
                            List<Loan> loanListResponses = response.body().loans;
                            Log.d(LOG_TAG, "APP populateLoansList Rx onNext with "
                                    + loanListResponses.size() + " items retreived.");
                            mLoanAdapter.setLoans(loanListResponses);
                        } else {
                            Log.d(LOG_TAG, "APP getLiveLoans onNext() status > "
                                    + response.code());
                            if (HTTPResponseUtils.check4xxClientError(response.code())) {
                                if (ConstantVariables.HTTP_UNAUTHORISED == response.code()) {
                                    //Unauthorised, Invalidate & Logout
                                    AuthAccountUtils.actionLogout(getActivity());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP populateLoansList Rx onComplete");
                        swipeContainer.setRefreshing(false);
                    }
                });
    }

    private void setSearchExpandedLayoutCollapse(){
        if(loanListSearchExpandableLayout != null){
            if(loanListSearchExpandableLayout.isExpanded()){
                Log.d(LOG_TAG, "APP loanListSearchExpandableLayout is collapsing.");
                loanListSearchExpandableLayout.collapse();
            }
        }
    }

    private void setSearchExpandedLayoutExpand(){
        if(loanListSearchExpandableLayout != null){
            if(!loanListSearchExpandableLayout.isExpanded()){
                Log.d(LOG_TAG, "APP loanListSearchExpandableLayout is expanding.");
                loanListSearchExpandableLayout.expand();
            }
        }
    }

    private void updateShoppingCartItemCount(){
        if(mMenuCart == null) {
            Log.d(LOG_TAG, "APP updateShoppingCartItemCount mMenuCart is null");
            getActivity().invalidateOptionsMenu();
            return;
        }

        final LayerDrawable cartIcon = (LayerDrawable) mMenuCart.getIcon();
        MemberDataRetrieval memberRetrieval = new MemberDataRetrieval();
        memberRetrieval.retrieveMemberInfo(getActivity(), new CallBackUtil<MemberInfoResponse>() {
            @Override
            public void eventCallBack(MemberInfoResponse memberInfoResponse) {
                if (HTTPResponseUtils.check2xxSuccess(memberInfoResponse
                        .getServerResponse().getStatus())) {
                    CartBadgeDrawable.setBadgeCount(getActivity(), cartIcon,
                            Integer.toString(memberInfoResponse.getNumberOfPendingBids()));
                }
            }
        });
    }

}