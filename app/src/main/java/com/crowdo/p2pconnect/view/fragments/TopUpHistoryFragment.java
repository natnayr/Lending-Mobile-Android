package com.crowdo.p2pconnect.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pconnect.R;

import butterknife.ButterKnife;

/**
 * Created by cwdsg05 on 13/7/17.
 */

public class TopUpHistoryFragment extends Fragment {

    private Context mContext;

    public TopUpHistoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_up_history, container, false);
        ButterKnife.bind(this, rootView);


        return rootView;
    }
}
