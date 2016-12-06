package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.MenuItem;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

public class DetailsActivity extends Activity {

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    public static final String TAG_DETAILS_FRAGMENT = "DetailsFragment";
    public static final String BUNDLE_LOANID_KEY = "BundleDetailsFragmentLoanIDKey";
    @InjectExtra public String loanID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Dart.inject(this);
        Log.d(LOG_TAG, "TEST: I have loanID: " + loanID);

        Bundle args = new Bundle();
        args.putString(BUNDLE_LOANID_KEY, loanID);

        Fragment fragment = getFragmentManager().findFragmentByTag(TAG_DETAILS_FRAGMENT);

        if(fragment == null) {
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_details, detailsFragment, TAG_DETAILS_FRAGMENT)
                    .addToBackStack(TAG_DETAILS_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    //If no backstack then navigate to logical parent
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
