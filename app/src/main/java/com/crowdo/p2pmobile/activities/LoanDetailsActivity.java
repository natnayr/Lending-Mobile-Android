package com.crowdo.p2pmobile.activities;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.fragments.LoanDetailsFragment;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoanDetailsActivity extends AppCompatActivity {

    public static final String BUNDLE_ID_KEY = "BundleDetailsFragmentIDKey";
    @InjectExtra public int id;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_details);
        ButterKnife.bind(this);

        //mToolbar view
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.activity_loan_detail_action_bar_label));
        LoanDetailsActivity.this.setTitle(getString(R.string.activity_loan_detail_action_bar_label));

        //enable back buttons
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Dart.inject(this);

        Bundle args = new Bundle();
        args.putInt(BUNDLE_ID_KEY, this.id);

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(LoanDetailsFragment.TAG_LOAN_DETAILS_FRAGMENT);

        if(fragment == null) {
            LoanDetailsFragment loanDetailsFragment = new LoanDetailsFragment();
            loanDetailsFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.loan_details_content, loanDetailsFragment,
                            LoanDetailsFragment.TAG_LOAN_DETAILS_FRAGMENT)
                    .addToBackStack(LoanDetailsFragment.TAG_LOAN_DETAILS_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            toBackStackOrParent();
        }
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
