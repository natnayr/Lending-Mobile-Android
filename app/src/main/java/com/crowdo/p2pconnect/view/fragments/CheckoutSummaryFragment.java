package com.crowdo.p2pconnect.view.fragments;

import android.accounts.AccountManager;
import android.content.Context;
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
import com.crowdo.p2pconnect.data.client.CheckoutClient;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.model.response.CheckoutSummaryResponse;
import com.crowdo.p2pconnect.oauth.AuthAccountUtils;
import com.crowdo.p2pconnect.view.adapters.CheckoutSummaryAdapter;
import com.crowdo.p2pconnect.viewholders.CheckoutSummaryViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 22/5/17.
 */

public class CheckoutSummaryFragment extends Fragment{

    @BindView(R.id.checkout_summary_recycler_view) RecyclerView mCheckoutSummaryRecyclerView;

    private static final String LOG_TAG = CheckoutSummaryFragment.class.getSimpleName();
    private Context mContext;
    private CheckoutSummaryViewHolder viewHolder;
    private CheckoutSummaryAdapter checkoutSummaryAdapter;
    private Disposable disposableGetCheckoutSummary;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_checkout_summary, container, false);
        ButterKnife.bind(this, rootView);

        mContext = getActivity();

        viewHolder = new CheckoutSummaryViewHolder(rootView, getActivity());
        viewHolder.initView();

        this.checkoutSummaryAdapter = new CheckoutSummaryAdapter(getActivity());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mCheckoutSummaryRecyclerView.setLayoutManager(mLayoutManager);
        mCheckoutSummaryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCheckoutSummaryRecyclerView.setAdapter(this.checkoutSummaryAdapter);

        viewHolder.mSummaryCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        viewHolder.mSummaryRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSummaryList();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateSummaryList();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(disposableGetCheckoutSummary != null){
            disposableGetCheckoutSummary.dispose();
        }
    }

    private void populateSummaryList(){

        Log.d(LOG_TAG, "APP populateSummaryList()");
        final String uniqueAndroidID = ConstantVariables.getUniqueAndroidID(mContext);

        //check authentication
        CheckoutClient.getInstance(getActivity())
                .getCheckoutSummary(uniqueAndroidID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CheckoutSummaryResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposableGetCheckoutSummary = d;
                    }

                    @Override
                    public void onNext(@NonNull Response<CheckoutSummaryResponse> response) {
                        if(response.isSuccessful()){
                            CheckoutSummaryResponse body = response.body();
                            List<Investment> investments = body.getBids();
                            List<Loan> loans = body.getLoans();
                            checkoutSummaryAdapter.setBiddingInvestmentsAndLoans(investments, loans, body.getNumOfPendingBids());
                            viewHolder.populateSummaryDetails(body.getTotalPendingBids(), body.getAvailableCashBalance());
                        }else{
                            Log.d(LOG_TAG, "APP getCheckoutSummary onNext() status > " + response.code());
                            if(HTTPResponseUtils.check4xxClientError(response.code())){
                                if(ConstantVariables.HTTP_UNAUTHORISED == response.code()){
                                    AuthAccountUtils.actionLogout(getActivity());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP getCheckoutSummary onComplete()");
                    }
                });

    }
}
