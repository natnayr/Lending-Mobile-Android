package com.crowdo.p2pmobile;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

public class DetailsActivity extends Activity {

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    @InjectExtra public String loanID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Dart.inject(this);

        Log.d(LOG_TAG, "TEST: " + "called");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_details, new DetailsFragment());
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case
        }

        return super.onOptionsItemSelected(item);
    }
}
