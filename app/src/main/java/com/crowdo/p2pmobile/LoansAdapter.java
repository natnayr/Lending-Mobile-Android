package com.crowdo.p2pmobile;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.crowdo.p2pmobile.data.APIServicesInterface;
import com.crowdo.p2pmobile.data.LoanItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cwdsg05 on 15/11/16.
 */

public class LoansAdapter extends BaseAdapter {

    private final String LOG_TAG = LoansAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<LoanItem> mLoanList;


    public LoansAdapter(Context context) {
        super();
        this.mContext = context;

        // yes, doing data retreival when the adapter loads itself
        populateLoansList();
    }

    private void populateLoansList(){
        mLoanList = new ArrayList<LoanItem>();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIServicesInterface.BASE_URL + APIServicesInterface.STAGE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        APIServicesInterface apiService = retrofit.create(APIServicesInterface.class);
        Call<List<LoanItem>> call = apiService.getLoansList();
        call.enqueue(loanCallback);
    }

    Callback loanCallback = new Callback<List<LoanItem>>() {
        @Override
        public void onResponse(Call<List<LoanItem>> call, final Response<List<LoanItem>> response) {
            if(response.isSuccessful()) {
                mLoanList = (ArrayList<LoanItem>) response.body();
                Log.d(LOG_TAG, "TEST: list size is " + mLoanList.size());
                notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Call<List<LoanItem>> call, Throwable t) {
            Log.e(LOG_TAG + " Error:", t.getMessage());
        }
    };

    @Override
    public View getView(final int position, View contextView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)
                this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(contextView == null){
            contextView = inflater.inflate(R.layout.list_item_loan, null);

            // get LoanItem populated
            LoanItem item = mLoanList.get(position);

            TextView mLoanId = (TextView) contextView.findViewById(R.id.loan_item_iden_no);
            mLoanId.setText(item.loanIdOut);

            TextView mLoanGrade = (TextView) contextView.findViewById(R.id.loan_item_credit_grade_text);
            mLoanGrade.setText(item.grade);


        }


        return contextView;
    }

    @Override
    public int getCount() {
        return mLoanList.size();
    }

    @Override
    public LoanItem getItem(int position) {
        return mLoanList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }



}
