package com.crowdo.p2pmobile;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.HensonNavigable;

public class MainActivity extends Activity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String TAG_LOAN_LIST_FRAGMENT = "LoanListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dart.inject(this);

        Log.d(LOG_TAG, "TEST: " + this.getClass().getSimpleName() + " is called");

        Fragment loanListFragment = getFragmentManager().findFragmentByTag(TAG_LOAN_LIST_FRAGMENT);
        if(loanListFragment == null){
            getFragmentManager().beginTransaction()
                    .add(new LoanListFragment(), TAG_LOAN_LIST_FRAGMENT)
                    .commit();
        }

    }
}
