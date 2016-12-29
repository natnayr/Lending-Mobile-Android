package com.crowdo.p2pmobile;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.HensonNavigable;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String TAG_LOAN_LIST_FRAGMENT = "LoanListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar view
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_loan_list);
        setSupportActionBar(toolbar);

        //load preferences
//        PreferenceManager.setDefaultValues(this, SharedPreferencesHelper.GLOBALKEYSTORE_HANDLE, Context.MODE_PRIVATE, R.);
        Dart.inject(this);

        Fragment loanListFragment = getFragmentManager().findFragmentByTag(TAG_LOAN_LIST_FRAGMENT);
        if(loanListFragment == null){
            getFragmentManager().beginTransaction()
                    .add(new LoanListFragment(), TAG_LOAN_LIST_FRAGMENT)
                    .commit();
        }

    }
}
