package com.crowdo.p2pconnect.view.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.model.response.LoanResponse;
import com.crowdo.p2pconnect.viewholders.LoanListViewHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by cwdsg05 on 15/11/16.
 */

public class LoanListAdapter extends BaseAdapter{

    private final String LOG_TAG = LoanListAdapter.class.getSimpleName();

    private Context mContext;
    private List<LoanResponse> mLoanList;
    private List<LoanResponse> mFilteredList;

    private String searchQuery;
    private List<String> gradesToFilter;
    private List<Integer> termsToFilter;
    private List<String> securityToFilter;

    private TextView filteringCountTextView;
    private String filteringCountTextViewTail;

    public LoanListAdapter(Context context) {
        super();
        this.mContext = context;
        this.searchQuery = new String("");
        this.gradesToFilter = new ArrayList<String>();
        this.termsToFilter = new ArrayList<Integer>();
        this.securityToFilter = new ArrayList<String>();
        this.mFilteredList = new ArrayList<LoanResponse>();
        this.mLoanList = new ArrayList<LoanResponse>();
    }

    @Override
    public int getCount() {
        return mFilteredList.size();
    }

    @Override
    public LoanResponse getItem(int position) {
        if(position < 0 || position >= mFilteredList.size()){
            return null;
        }else{
            return mFilteredList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View contextView, ViewGroup parent) {
        final View view = (contextView != null ? contextView : createView(parent));
        final LoanListViewHolder viewHolder = (LoanListViewHolder) view.getTag();
        viewHolder.attachLoanItem(getItem(position), mContext);
        return view;
    }

    private View createView(ViewGroup parent){
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_loan_list, parent, false);
        final LoanListViewHolder loanListViewHolder = new LoanListViewHolder(mContext, view);
        view.setTag(loanListViewHolder);
        return view;
    }

    public void setLoans(@Nullable List<LoanResponse> retreivedLoans){
        if(retreivedLoans == null){
            return;
        }
        mLoanList.clear();
        mFilteredList.clear();
        mLoanList.addAll(retreivedLoans);
        mFilteredList.addAll(retreivedLoans);
        notifyDataSetChanged();
        if(filteringCountTextView != null){
            filteringCountTextView.setText(Integer.toString(getCount())
                    + filteringCountTextViewTail);
        }
        searchLoans();
    }

    public void setFilteringCountTextView(TextView filteringCountTextView, String filteringCountTextViewTail) {
        this.filteringCountTextView = filteringCountTextView;
        this.filteringCountTextViewTail = filteringCountTextViewTail;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public List<String> getGradesToFilter() {
        return gradesToFilter;
    }

    public List<Integer> getTermsToFilter() {
        return termsToFilter;
    }

    public List<String> getSecurityToFilter() {
        return securityToFilter;
    }

    public void searchLoans(){

        if(this.searchQuery == null || this.gradesToFilter == null || this.termsToFilter == null
                || this.securityToFilter == null){
            //if any input null, do nothing
            return;
        }

        if(this.searchQuery.isEmpty() && this.gradesToFilter.isEmpty() && this.termsToFilter.isEmpty()
                && this.securityToFilter.isEmpty()){
            mFilteredList.clear();
            mFilteredList.addAll(mLoanList);
            notifyDataSetChanged();
        }else{
            mFilteredList.clear();
            mFilteredList.addAll(mLoanList);
            Iterator<LoanResponse> llit = mFilteredList.iterator();
            //fill and pick-off method

            while(llit.hasNext()){
                LoanResponse item = llit.next();

                if(!"".contains(this.searchQuery)){
                    if(!item.getLoanId().toLowerCase().trim()
                            .contains(this.searchQuery.toLowerCase().trim())) {
                        Log.d(LOG_TAG, "APP filtering by search = "
                                + item.getLoanId().toLowerCase().trim());
                        llit.remove();
                        continue;
                    }
                }

                if(!gradesToFilter.isEmpty()){
                    if(!gradesToFilter.contains(item.getGrade())){
                        Log.d(LOG_TAG, "APP filtering by grade = "
                                + item.getGrade());
                        llit.remove();
                        continue;
                    }
                }

                if (!termsToFilter.isEmpty()){
                    if(!termsToFilter.contains(item.getTenure())){
                        Log.d(LOG_TAG, "APP filtering by term = "
                                + item.getTenure());
                        llit.remove();
                        continue;
                    }
                }

                if(!securityToFilter.isEmpty()){
                    if(!securityToFilter.contains(item.getSecurity())){
                        Log.d(LOG_TAG, "APP filtering by security = "
                                + item.getSecurity());
                        llit.remove();
                        continue;
                    }
                }
            }
            notifyDataSetChanged();
        }

        if(filteringCountTextView != null){
            filteringCountTextView.setText(Integer.toString(getCount()) + filteringCountTextViewTail);
        }
    }

}
