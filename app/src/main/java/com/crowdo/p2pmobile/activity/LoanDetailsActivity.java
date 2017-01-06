package com.crowdo.p2pmobile.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.fragment.LoanDetailsFragment;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoanDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoanDetailsActivity.class.getSimpleName();
    public static final String TAG_LOAN_DETAILS_FRAGMENT = "LoanDetailsFragment";
    public static final String BUNDLE_ID_KEY = "BundleDetailsFragmentIDKey";
    @InjectExtra public int id;
    @BindView(R.id.toolbar_loan_details) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        //toolbar view
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu);

        //enable back buttons
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Dart.inject(this);

        Bundle args = new Bundle();
        args.putInt(BUNDLE_ID_KEY, this.id);

        Fragment fragment = getFragmentManager().findFragmentByTag(TAG_LOAN_DETAILS_FRAGMENT);

        if(fragment == null) {
            LoanDetailsFragment loanDetailsFragment = new LoanDetailsFragment();
            loanDetailsFragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .replace(R.id.loan_details_content, loanDetailsFragment, TAG_LOAN_DETAILS_FRAGMENT)
                    .addToBackStack(TAG_LOAN_DETAILS_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
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
                onBackPressed();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
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
