package com.crowdo.p2pconnect.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.WalletClient;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.model.response.WithdrawHistoryResponse;
import com.crowdo.p2pconnect.support.InvestorAccreditationReaction;
import com.crowdo.p2pconnect.view.adapters.WithdrawHistoryAdapter;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 11/8/17.
 */

public class WithdrawHistoryFragment extends Fragment{

    @BindView(R.id.withdraw_history_recycle_view) RecyclerView mWithdrawHistoryRecyclerView;
    @BindString(R.string.withdraw_invalid_investor_label) String mInvalidInvestorLabel;
    @BindString(R.string.withdraw_invalid_investor_button_label) String mInvalidInvestorButtonLabel;

    private static final String LOG_TAG = WithdrawHistoryFragment.class.getSimpleName();
    private WithdrawHistoryAdapter mWithdrawHistoryAdapter;
    private Disposable disposableGetWithdrawHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_withdraw_history, container, false);
        ButterKnife.bind(this, rootView);

        this.mWithdrawHistoryAdapter = new WithdrawHistoryAdapter(getActivity());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mWithdrawHistoryRecyclerView.setLayoutManager(mLayoutManager);
        mWithdrawHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mWithdrawHistoryRecyclerView.setAdapter(this.mWithdrawHistoryAdapter);

        populateWithdrawHistory();

        return rootView;
    }

    public void populateWithdrawHistory(){
        Log.d(LOG_TAG, "APP populateWithdrawHistory");

        final WalletClient walletClient = WalletClient.getInstance(getActivity());

        walletClient.getWithdrawHistory(ConstantVariables.getUniqueAndroidID(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<WithdrawHistoryResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableGetWithdrawHistory = d;
                    }

                    @Override
                    public void onNext(Response<WithdrawHistoryResponse> response) {
                        if(response.isSuccessful()){
                            WithdrawHistoryResponse withdrawHistoryResponse = response.body();

                            WithdrawHistoryFragment.this.mWithdrawHistoryAdapter
                                    .setWalletEntryHistoryList(withdrawHistoryResponse.getWalletEntries());
                        }else {
                            Log.d(LOG_TAG, "APP getWithdrawHistory onNext() status > " + response.code());
                            if(HTTPResponseUtils.check4xxClientError(response.code())){
                                if (ConstantVariables.HTTP_INVESTOR_FAILED_ACCREDITATION == response.code()){

                                    Snackbar investorInvalidSnackbar = InvestorAccreditationReaction
                                            .failedInvestorAcreditationSnackbar(
                                                    mInvalidInvestorLabel, mInvalidInvestorButtonLabel,
                                                    getView(), getActivity());
                                    investorInvalidSnackbar.show();
                                }else {
                                    String serverErrorMessage = HTTPResponseUtils
                                            .errorServerResponseConvert(walletClient,
                                                    response.errorBody());

                                    SnackBarUtil.snackBarForWarningCreate(getView(),
                                            serverErrorMessage, Snackbar.LENGTH_SHORT)
                                            .show();
                                }}
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP getWithdrawHistory onComplete()");
                    }
                });
    }

    public void refreshFragment(){
        Log.d(LOG_TAG, "APP refreshFragment called");
        FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
        ftr.detach(WithdrawHistoryFragment.this);
        ftr.attach(WithdrawHistoryFragment.this);
        ftr.commit();
    }

    @Override
    public void onPause() {
        if(disposableGetWithdrawHistory != null){
            if(!disposableGetWithdrawHistory.isDisposed()){
                disposableGetWithdrawHistory.dispose();
            }
        }

        super.onPause();
    }
}
