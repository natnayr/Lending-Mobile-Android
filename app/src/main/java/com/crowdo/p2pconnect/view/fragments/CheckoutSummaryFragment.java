package com.crowdo.p2pconnect.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.model.others.CheckoutSummaryBid;
import com.crowdo.p2pconnect.view.adapters.CheckoutSummaryAdapter;
import com.crowdo.p2pconnect.viewholders.CheckoutSummaryViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 22/5/17.
 */

public class CheckoutSummaryFragment extends Fragment{

    @BindView(R.id.checkout_summary_recycler_view) RecyclerView mCheckoutSummaryRecyclerView;

    private static final String LOG_TAG = CheckoutSummaryFragment.class.getSimpleName();
    private CheckoutSummaryViewHolder viewHolder;
    private List<CheckoutSummaryBid> summaryList;
    private CheckoutSummaryAdapter checkoutSummaryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.summaryList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_checkout_summary, container, false);
        ButterKnife.bind(this, rootView);

        viewHolder = new CheckoutSummaryViewHolder(rootView, getActivity());
        viewHolder.initView();

        this.checkoutSummaryAdapter = new CheckoutSummaryAdapter(summaryList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mCheckoutSummaryRecyclerView.setLayoutManager(mLayoutManager);
        mCheckoutSummaryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCheckoutSummaryRecyclerView.setAdapter(this.checkoutSummaryAdapter);

        populateFakeData();

        return rootView;
    }

    private void populateFakeData(){

        CheckoutSummaryBid demoItem1 = new CheckoutSummaryBid("CWDO35870002", 9, "C", 27.0, 5000000);
        CheckoutSummaryBid demoItem2 = new CheckoutSummaryBid("CWDO35873212", 6, "C+", 21.0, 17000000);
        CheckoutSummaryBid demoItem3 = new CheckoutSummaryBid("CWDO32327824", 3, "A+", 14.0, 25000000);
        summaryList.add(demoItem1);
        summaryList.add(demoItem2);
        summaryList.add(demoItem3);

        this.checkoutSummaryAdapter.notifyDataSetChanged();
    }
}
