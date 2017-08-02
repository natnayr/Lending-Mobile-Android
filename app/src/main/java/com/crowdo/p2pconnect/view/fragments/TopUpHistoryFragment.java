package com.crowdo.p2pconnect.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.WalletClient;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.model.response.TopUpHistoryResponse;
import com.crowdo.p2pconnect.view.adapters.TopUpHistoryAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 13/7/17.
 */

public class TopUpHistoryFragment extends Fragment {

    @BindView(R.id.top_up_history_recycler_view) RecyclerView mTopUpHistoryRecyclerView;

    private TopUpHistoryAdapter mTopUpHistoryAdapter;

    private static final String LOG_TAG = TopUpHistoryFragment.class.getSimpleName();

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

        this.mTopUpHistoryAdapter = new TopUpHistoryAdapter();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mTopUpHistoryRecyclerView.setLayoutManager(mLayoutManager);
        mTopUpHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTopUpHistoryRecyclerView.setAdapter(this.mTopUpHistoryAdapter);


        return rootView;
    }

    public void populateTopUpHistory(){
        Log.d(LOG_TAG, "APP populateTopUpHistory");

        WalletClient.getInstance(getActivity())
                .getTopUpHistory(ConstantVariables.getUniqueAndroidID(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<TopUpHistoryResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<TopUpHistoryResponse> topUpHistoryResponseResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
    }
}
