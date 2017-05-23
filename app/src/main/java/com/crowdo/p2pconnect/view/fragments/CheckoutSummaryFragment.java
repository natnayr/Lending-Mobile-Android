package com.crowdo.p2pconnect.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.viewholders.CheckoutSummaryViewHolder;

import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 22/5/17.
 */

public class CheckoutSummaryFragment extends Fragment{

    private static final String LOG_TAG = CheckoutSummaryFragment.class.getSimpleName();
    private CheckoutSummaryViewHolder viewHolder;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_checkout_summary, container, false);
        ButterKnife.bind(this, rootView);

        viewHolder = new CheckoutSummaryViewHolder(rootView);

        mContext = getActivity();



        return rootView;
    }
}
