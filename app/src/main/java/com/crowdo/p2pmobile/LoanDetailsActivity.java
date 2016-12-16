package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

public class LoanDetailsActivity extends Activity {

    private static final String LOG_TAG = LoanDetailsActivity.class.getSimpleName();
    public static final String TAG_LOAN_DETAILS_FRAGMENT = "LoanDetailsFragment";
    public static final String BUNDLE_ID_KEY = "BundleDetailsFragmentIDKey";
    @InjectExtra public int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Dart.inject(this);

        Bundle args = new Bundle();
        args.putInt(BUNDLE_ID_KEY, this.id);

        Fragment fragment = getFragmentManager().findFragmentByTag(TAG_LOAN_DETAILS_FRAGMENT);

        if(fragment == null) {
            LoanDetailsFragment loanDetailsFragment = new LoanDetailsFragment();
            loanDetailsFragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .replace(R.id.loan_details, loanDetailsFragment, TAG_LOAN_DETAILS_FRAGMENT)
                    .addToBackStack(TAG_LOAN_DETAILS_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            toBackStackOrParent();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                return toBackStackOrParent();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean toBackStackOrParent(){
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
        } else {
            //If no backstack then navigate to logical main list view
            NavUtils.navigateUpTo(this, upIntent);
        }
        return true;
    }
}
