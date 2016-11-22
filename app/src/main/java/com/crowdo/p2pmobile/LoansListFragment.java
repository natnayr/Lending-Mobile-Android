package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.crowdo.p2pmobile.data.LoanItem;


/**
 * Created by cwdsg05 on 8/11/16.
 */

public class LoansListFragment extends Fragment {

    private static final String LOG_TAG = LoansListFragment.class.getSimpleName();

    public ListView mListView;
    public LoansAdapter mLoanAdapter;

    public LoansListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listview_loans);
        mLoanAdapter = new LoansAdapter(getActivity());
        mListView.setAdapter(mLoanAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long l) {
                Toast.makeText(getActivity(), "hello, im at position " + position, Toast.LENGTH_LONG).show();

                LoanItem item = (LoanItem) adapterView.getItemAtPosition(position);
            }
        });
        return rootView;
    }

}
