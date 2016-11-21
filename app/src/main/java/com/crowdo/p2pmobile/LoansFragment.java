package com.crowdo.p2pmobile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.crowdo.p2pmobile.data.APIServicesInterface;
import com.crowdo.p2pmobile.data.LoanItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.support.v4.app.Fragment;

/**
 * Created by cwdsg05 on 8/11/16.
 */

public class LoansFragment extends Fragment {

    private static final String LOG_TAG = LoansFragment.class.getSimpleName();
    private List<LoanItem> resultLoanList;

    private LoansAdapter adapter;
    private FragmentActivity listener;

    @BindView(R.id.listview_loans) ListView mListView;


    public LoansFragment(){
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity){

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, this.getClass().getSimpleName() + " is called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        Log.d(LOG_TAG, "TEST: " + this.getClass().getSimpleName() + " is called");

        View rootView = inflater.inflate(R.layout.fragment_main, parent, false);
        ButterKnife.bind(this, rootView);

//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd")
//                .create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(APIServicesInterface.BASE_URL + APIServicesInterface.STAGE)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        APIServicesInterface apiService = retrofit.create(APIServicesInterface.class);
//        Call<List<LoanItem>> call = apiService.getLoansList();
//
//        call.enqueue(new Callback<List<LoanItem>>(){
//            @Override
//            public void onResponse(Call<List<LoanItem>> call, Response<List<LoanItem>> response) {
//                resultLoanList = response.body();
//                logPrintLoan();
//            }
//
//            @Override
//            public void onFailure(Call<List<LoanItem>> call, Throwable t) {
//
//            }
//        });



        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //handlers to view objects here
    }

    //    private void logPrintLoan(){
//        Iterator<LoanItem> lit = resultLoanList.iterator();
//        while(lit.hasNext()){
//            LoanItem item = lit.next();
//            Log.d(LOG_TAG, "TEST: " + item.loanIdOut);
//        }
//    }
}
