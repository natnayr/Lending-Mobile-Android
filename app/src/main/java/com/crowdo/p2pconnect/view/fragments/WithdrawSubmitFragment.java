package com.crowdo.p2pconnect.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pconnect.R;

import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 11/8/17.
 */

public class WithdrawSubmitFragment extends Fragment {

    private static final String LOG_TAG = WithdrawSubmitFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_withdraw_submit, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }
}
