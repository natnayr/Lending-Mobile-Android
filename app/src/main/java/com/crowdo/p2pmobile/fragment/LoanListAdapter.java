package com.crowdo.p2pmobile.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.data.LoanListItem;
import com.crowdo.p2pmobile.helper.CustomDateHelper;
import com.crowdo.p2pmobile.helper.CustomNumberFormatter;
import com.crowdo.p2pmobile.helper.FontManager;
import com.crowdo.p2pmobile.viewholder.LoanListViewHolder;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by cwdsg05 on 15/11/16.
 */

public class LoanListAdapter extends BaseAdapter {

    private final String LOG_TAG = LoanListAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<LoanListItem> mLoanList = new ArrayList<LoanListItem>();

    public LoanListAdapter(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mLoanList.size();
    }

    @Override
    public LoanListItem getItem(int position) {
        if(position < 0 || position >= mLoanList.size()){
            return null;
        }else{
            return mLoanList.get(position);
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
        final View view = inflater.inflate(R.layout.list_item_loan, parent, false);
        final LoanListViewHolder loanListViewHolder = new LoanListViewHolder(mContext, view);
        view.setTag(loanListViewHolder);
        return view;
    }

    public void setLoans(@Nullable List<LoanListItem> retreivedLoans){
        if(retreivedLoans == null){
            return;
        }
        mLoanList.clear();
        mLoanList.addAll(retreivedLoans);
        notifyDataSetChanged();
    }

}
